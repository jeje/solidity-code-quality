package com.talanlabs.solidity;

import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;
import com.talanlabs.solidity.rules.FallbackRuleChecker;
import com.talanlabs.solidity.rules.PayableRuleChecker;
import com.talanlabs.solidity.rules.ReEntrancyRuleChecker;
import com.talanlabs.solidity.rules.TxOriginRuleChecker;
import org.antlr.v4.runtime.*;
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

    /*
    @Test
    public void test_reentrancy_checks() throws IOException {
        SourceUnitContext tree = parse("src/test/antlr/ReEntrancyContract.sol");
        RuleChecker visitor = new ReEntrancyRuleChecker();
        ValidationResults results = visitor.visit(tree);
        assertEquals(1, results.getErrors().size());
        ValidationError error = results.getErrors().get(0);
        assertEquals(new ValidationError("re-entrancy", ValidationErrorCriticity.BLOCKER,
                "Function withdraw() is vulnerable to re-entrancy", new Position(10,9), new Position(11,31)), error);
    }

    @Test
    public void test_payable_checks() throws IOException {
        SourceUnitContext tree = parse("src/test/antlr/PayableContract.sol");
        RuleChecker visitor = new PayableRuleChecker();
        ValidationResults results = visitor.visit(tree);
        assertEquals(1, results.getErrors().size());
        ValidationError error = results.getErrors().get(0);
        assertEquals(new ValidationError("payable", ValidationErrorCriticity.INFO,
                "Function getCash() is a payable method. Take care!", new Position(8,5), new Position(10,5)), error);
    }

    @Test
    public void test_fallback_checks() throws IOException {
        SourceUnitContext tree = parse("src/test/antlr/FallbackContract.sol");
        RuleChecker visitor = new FallbackRuleChecker();
        ValidationResults results = visitor.visit(tree);
        assertEquals(1, results.getErrors().size());
        ValidationError error = results.getErrors().get(0);
        assertEquals(new ValidationError("fallback", ValidationErrorCriticity.INFO,
                "Fallback function detected. Take care!", new Position(5,5), new Position(7,5)), error);
    }
    */

    private static SourceUnitContext parse(String fileName) throws IOException {
        CharStream input = CharStreams.fromFileName(fileName);
        SolidityLexer lexer = new SolidityLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SolidityParser parser = new SolidityParser(tokens);
        parser.setBuildParseTree(true);
        return parser.sourceUnit();
    }

}
