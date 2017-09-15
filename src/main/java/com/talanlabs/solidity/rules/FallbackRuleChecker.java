package com.talanlabs.solidity.rules;

import com.talanlabs.solidity.RuleChecker;
import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;

import static com.talanlabs.solidity.SolidityParser.*;

/**
 * Rule check for fallbacks methods.
 */
public class FallbackRuleChecker extends RuleChecker {
    @Override
    public ValidationResults visitFunctionDefinition(FunctionDefinitionContext ctx) {
        if (ctx.identifier() == null) {
            addValidationError(new ValidationError("fallback", ValidationErrorCriticity.INFO,
                    "Fallback function detected. Take care!", Position.start(ctx.start), Position.stop(ctx.stop))
            );
        }
        return super.visitFunctionDefinition(ctx);
    }
}
