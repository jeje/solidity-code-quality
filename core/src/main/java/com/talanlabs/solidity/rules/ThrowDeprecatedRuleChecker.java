package com.talanlabs.solidity.rules;

import com.talanlabs.solidity.RuleChecker;
import com.talanlabs.solidity.SolidityParser;
import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;

/**
 * Checking usage of throw statement, especially in <tt>if</tt> blocks.
 * See <a href="https://media.consensys.net/when-to-use-revert-assert-and-require-in-solidity-61fb2c0e5a57">https://media.consensys.net/when-to-use-revert-assert-and-require-in-solidity-61fb2c0e5a57</a>
 */

public class ThrowDeprecatedRuleChecker extends RuleChecker {

    @Override
    public ValidationResults visitThrowStatement(SolidityParser.ThrowStatementContext ctx) {
        SolidityParser.IfStatementContext ifStatementContext = inIfClause(ctx);
        if (ifStatementContext != null) {
            addValidationError(new ValidationError(getRuleCode(), getRuleCriticity(),
                    getRuleDescription(),
                    Position.start(ifStatementContext.start), Position.stop(ifStatementContext.stop))
            );
        }
        return super.visitThrowStatement(ctx);
    }

    @Override
    public String getRuleCode() {
        return "throw-deprecated";
    }

    @Override
    public String getRuleDescription() {
        return "Throw is deprecated. Use require(), revert() or assert() instead";
    }

    @Override
    public ValidationErrorCriticity getRuleCriticity() {
        return ValidationErrorCriticity.MAJOR;
    }
}
