package com.talanlabs.solidity.rules;

import com.talanlabs.solidity.RuleChecker;
import com.talanlabs.solidity.SolidityParser;
import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;
import org.antlr.v4.runtime.ParserRuleContext;

public class ReEntrancyRuleChecker extends RuleChecker {

    @Override
    public ValidationResults visitExpression(SolidityParser.ExpressionContext ctx) {
        SolidityParser.IdentifierContext identifier = ctx.identifier();
        if (identifier != null && "send".equals(identifier.getText())) {
            ParserRuleContext parent = ctx.getParent();
            while (!parent.getClass().isAssignableFrom(SolidityParser.IfStatementContext.class)) {
                parent = parent.getParent();
            }
            addValidationError(new ValidationError("re-entrancy", ValidationErrorCriticity.BLOCKER,
                    "Function withdraw() is vulnerable to re-entrancy", Position.start(parent.start), Position.stop(parent.stop))
            );
        }
        return super.visitExpression(ctx);
    }

    @Override
    public ValidationResults visitIdentifierList(SolidityParser.IdentifierListContext ctx) {
        if ("send".equals(ctx.getText()))
            addValidationError(new ValidationError("re-entrancy", ValidationErrorCriticity.BLOCKER,
                    "Function withdraw() is vulnerable to re-entrancy", Position.start(ctx.start), Position.stop(ctx.stop))
            );
        return super.visitIdentifierList(ctx);
    }

}
