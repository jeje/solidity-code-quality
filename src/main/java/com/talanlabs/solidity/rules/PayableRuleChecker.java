package com.talanlabs.solidity.rules;

import com.talanlabs.solidity.RuleChecker;
import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import static com.talanlabs.solidity.SolidityParser.*;

/**
 * Rule check for payable methods.
 *
 * This rule check only generates INFO messages about Solidity functions which should be given great care.
 */
public class PayableRuleChecker extends RuleChecker {
    @Override
    public ValidationResults visitFunctionDefinition(FunctionDefinitionContext ctx) {
        IdentifierContext identifier = ctx.identifier();
        ModifierListContext modifiers = ctx.modifierList();
        List<TerminalNode> payable = modifiers.PayableKeyword();
        for (TerminalNode node : payable) {
            addValidationError(new ValidationError("payable", ValidationErrorCriticity.INFO,
                    String.format("Function %s() is a payable method. Take care!", identifier.getText()),
                    Position.start(ctx.start), Position.stop(ctx.stop)));
        }
        return super.visitFunctionDefinition(ctx);
    }
}
