package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationResults;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.List;

public class Scanner {
    private final List<RuleChecker> rules;

    public Scanner() {
        rules = new RulesRepository().getRules();
    }

    public ValidationResults scan(String contract) throws IOException {
        System.out.println("Running Solidity Analyzer on '" + contract + "'...");
        SolidityParser.SourceUnitContext tree = parse(contract);
        ValidationResults globalResults = new ValidationResults();
        for (RuleChecker rule : rules) {
            ValidationResults results = rule.visit(tree);
            List<ValidationError> errors = results.getErrors();
            // add each error to the global validation errors
            errors.forEach(globalResults::addValidationError);
        }
        // dump errors found
        List<ValidationError> errors = globalResults.getErrors();
        if (!errors.isEmpty()) {
            System.out.printf("%s error(s) found!%n", errors.size());
            for (ValidationError error : errors) {
                System.out.printf("[%s] %s error ('%s') %s%n",
                        error.getCriticity(),
                        error.getCode(),
                        error.getMessage(),
                        String.format("L%d:C%d -> L%d:C%d",
                                error.getStart().getLine(), error.getStart().getColumn(),
                                error.getStop().getLine(), error.getStop().getColumn()
                        ));
            }
        }
        return globalResults;
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
