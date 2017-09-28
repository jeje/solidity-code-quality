package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationResults;
import com.talanlabs.solidity.rules.ThrowDeprecatedRuleChecker;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import org.sonar.api.batch.fs.internal.FileExtensionPredicate;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.issue.Issuable;
import org.sonar.api.rule.RuleKey;

import java.io.IOException;
import java.util.List;

import static com.talanlabs.solidity.SolidityPlugin.LANGUAGE_KEY;
import static com.talanlabs.solidity.SolidityRulesDefinition.REPOSITORY_KEY;

public class SoliditySensor implements Sensor {
    private static final Logger LOG = LoggerFactory.getLogger(SoliditySensor.class);

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("Solidity Sensor");
        descriptor.createIssuesForRuleRepository(REPOSITORY_KEY);
    }

    @Override
    public void execute(SensorContext context) {
        FileSystem fs = context.fileSystem();
        Iterable<InputFile> files = fs.inputFiles(new FileExtensionPredicate("sol"));
        files.forEach(file -> analyzeContract(context, file));
    }

    private void analyzeContract(SensorContext context, InputFile file) {
        try {
            LOG.info("Analyzing Solidity contract '{}'...", file.absolutePath());
            SolidityParser.SourceUnitContext tree = parse(file.absolutePath());
            RuleChecker visitor = new ThrowDeprecatedRuleChecker();
            ValidationResults results = visitor.visit(tree);
            List<ValidationError> errors = results.getErrors();
            LOG.info("Found {} error(s)", errors.size());

            // loop through all detected errors and generate appropriate Sonar errors
            for (ValidationError error : errors) {
                NewIssue issue = context.newIssue()
                        .forRule(RuleKey.of(REPOSITORY_KEY, error.getCode()));
                LOG.info("Found {} issue: {} in file {}", error.getCriticity(), error.getCode(), file.absolutePath());
                issue.at(
                        issue.newLocation()
                                .on(file)
                                .message(error.getMessage())
                                .at(new DefaultTextRange(
                                        new DefaultTextPointer(error.getStart().getLine(), error.getStart().getColumn()),
                                        new DefaultTextPointer(error.getStop().getLine(), error.getStop().getColumn()))
                                )
                );
                issue.save();
            }
        } catch (IOException e) {
            LOG.error("Can't analyze contract '" + file.absolutePath() + "'", e);
        }
    }

    private Severity convertSeverity(ValidationError error) {
        switch (error.getCriticity()) {
            case BLOCKER:
                return Severity.BLOCKER;
            case CRITICAL:
                return Severity.CRITICAL;
            case MAJOR:
                return Severity.MAJOR;
            case MINOR:
                return Severity.MINOR;
        }
        throw new UnsupportedOperationException("Can't convert " + error.getCriticity() + " to Sonar criticity");
    }

    private static SolidityParser.SourceUnitContext parse(String fileName) throws IOException {
        CharStream input = CharStreams.fromFileName(fileName);
        SolidityLexer lexer = new SolidityLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SolidityParser parser = new SolidityParser(tokens);
        parser.setBuildParseTree(true);
        return parser.sourceUnit();
    }

}
