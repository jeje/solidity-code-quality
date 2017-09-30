package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationResults;
import com.talanlabs.solidity.rules.ThrowDeprecatedRuleChecker;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.List;

public class Main {
    @Option(name = "-f", usage = "Solidity contract to analyze", required = true)
    private String contract;

    public static void main(String[] args) {
        Main instance = new Main();
        final CmdLineParser parser = new CmdLineParser(instance);
        if (args.length < 1) {
            parser.printUsage(System.out);
            System.exit(-1);
        }
        try {
            parser.parseArgument(args);
            instance.analyzeContract();
        } catch (CmdLineException clEx) {
            System.err.println("ERROR: invalid arguments");
            parser.printUsage(System.out);
        }
    }

    private void analyzeContract() {
        System.out.println("Running Solidity Analyzer on '" + contract + "'...");
        try {
            SolidityParser.SourceUnitContext tree = parse(contract);
            RuleChecker visitor = new ThrowDeprecatedRuleChecker();
            ValidationResults results = visitor.visit(tree);
            List<ValidationError> errors = results.getErrors();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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
