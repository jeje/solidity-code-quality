package com.talanlabs.solidity;

import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;
import com.talanlabs.solidity.rules.ThrowDeprecatedRuleChecker;
import com.talanlabs.solidity.rules.TxOriginRuleChecker;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.io.IOException;

import static com.talanlabs.solidity.SolidityParser.SourceUnitContext;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void test_txorigin_checks() throws IOException {
        SourceUnitContext tree = parse("src/test/antlr/TxOriginContract.sol");
        RuleChecker visitor = new TxOriginRuleChecker();
        ValidationResults results = visitor.visit(tree);
        assertEquals(5, results.getErrors().size());
        ValidationError firstError = results.getErrors().get(0);
        assertEquals(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                "Potential vulnerability to tx.origin attack", new Position(13,17), new Position(13,34)), firstError);
        ValidationError secondError = results.getErrors().get(1);
        assertEquals(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                "Potential vulnerability to tx.origin attack", new Position(18,17), new Position(18,34)), secondError);
        ValidationError thirdError = results.getErrors().get(2);
        assertEquals(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                "Potential vulnerability to tx.origin attack", new Position(23,17), new Position(24,21)), thirdError);
        ValidationError fourthError = results.getErrors().get(3);
        assertEquals(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                "Potential vulnerability to tx.origin attack", new Position(31,17), new Position(31,26)), fourthError);
        ValidationError fifthError = results.getErrors().get(4);
        assertEquals(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                "Potential vulnerability to tx.origin attack", new Position(38,17), new Position(38,26)), fifthError);
    }

    @Test
    public void test_throw_deprecation_checks() throws IOException {
        SourceUnitContext tree = parse("src/test/antlr/ThrowDeprecationContract.sol");
        RuleChecker visitor = new ThrowDeprecatedRuleChecker();
        ValidationResults results = visitor.visit(tree);
        assertEquals(1, results.getErrors().size());
        ValidationError firstError = results.getErrors().get(0);
        assertEquals(new ValidationError("throw-deprecated", ValidationErrorCriticity.MAJOR,
                "Throw is deprecated. Use require(), revert() or assert() instead", new Position(9, 9), new Position(11, 9)), firstError);
    }

    private static SourceUnitContext parse(String fileName) throws IOException {
        CharStream input = CharStreams.fromFileName(fileName);
        SolidityLexer lexer = new SolidityLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SolidityParser parser = new SolidityParser(tokens);
        parser.setBuildParseTree(true);
        return parser.sourceUnit();
    }

}
