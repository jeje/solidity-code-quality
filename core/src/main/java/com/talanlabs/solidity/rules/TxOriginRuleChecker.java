package com.talanlabs.solidity.rules;

import com.talanlabs.solidity.RuleChecker;
import com.talanlabs.solidity.SolidityParser;
import com.talanlabs.solidity.model.Position;
import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

import static com.talanlabs.solidity.SolidityParser.ExpressionContext;
import static com.talanlabs.solidity.SolidityParser.VariableDeclarationContext;

/**
 * Checking ownership based on <tt>tx.origin</tt> is vulnerable.
 * See <a href="http://solidity.readthedocs.io/en/latest/security-considerations.html#tx-origin">http://solidity.readthedocs.io/en/latest/security-considerations.html#tx-origin</a>
 */
public class TxOriginRuleChecker extends RuleChecker {
    private final List<String> variablesLinkedToTxOrigin = new ArrayList<>();
    private final List<String> variablesLinkedToMsgSender = new ArrayList<>();

    @Override
    public ValidationResults visitExpression(ExpressionContext ctx) {
        List<ParseTree> children = ctx.children;
        // matches 'tx.origin' expression?
        ParseTree firstChildren = children.get(0);
        // comparison of 'tx.origin' with something?
        if (isExpression(firstChildren)
                && isDot(children.get(1))
                && isIdentifier("origin", children.get(2))
                && isComparedToMsgSender(ctx)) {
            ParserRuleContext parent = ctx.getParent();
            addValidationError(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                    "Potential vulnerability to tx.origin attack",
                    Position.start(parent.start),
                    Position.stop(parent.stop)
            ));
        }
        // comparison of a variable linked to 'tx.origin' with something?
        if (variablesLinkedToTxOrigin.contains(firstChildren.getText()) && isComparedToMsgSender(ctx)) {
            ParserRuleContext parent = ctx.getParent();
            addValidationError(new ValidationError("tx-origin", ValidationErrorCriticity.CRITICAL,
                    "Potential vulnerability to tx.origin attack",
                    Position.start(parent.start),
                    Position.stop(parent.stop)
            ));
        }
        // memorize variable name if assigned 'msg.sender' value
        if (isVariableAssignedTo(ctx, "msg.sender")) {
            variablesLinkedToMsgSender.add(firstChildren.getText());
        }
        return super.visitExpression(ctx);
    }

    @Override
    public ValidationResults visitVariableDeclaration(VariableDeclarationContext ctx) {
        String var = ctx.identifier().getText();
        ParserRuleContext parent = ctx.getParent();
        ParseTree firstSibling = parent.children.get(1);
        if (isTerminalNode("=", firstSibling)) {
            ParseTree secondSibling = parent.children.get(2);
            String value = secondSibling.getText();
            if ("tx.origin".equals(secondSibling.getText())) {
                variablesLinkedToTxOrigin.add(var);
            } else if ("msg.sender".equals(secondSibling.getText())) {
                variablesLinkedToMsgSender.add(var);
            }
        }
        return super.visitVariableDeclaration(ctx);
    }

    private boolean isComparedToMsgSender(SolidityParser.ExpressionContext ctx) {
        ParserRuleContext parent = ctx.getParent();
        boolean isParentAnExpression = SolidityParser.ExpressionContext.class.isAssignableFrom(parent.getClass());
        List<ParseTree> siblings = parent.children;
        boolean comparedToSomething = siblings.stream()
                .anyMatch(parseTree ->
                        TerminalNode.class.isAssignableFrom(parseTree.getClass())
                                && "==".equals(parseTree.getText())
                );
        boolean comparedToMsgSender = siblings.stream()
                .anyMatch(parseTree ->
                    isExpression(parseTree) && variablesLinkedToMsgSender.contains(parseTree.getText())
                );
        return isParentAnExpression && siblings.size() > 2 && comparedToSomething && comparedToMsgSender;
    }

    private boolean isDot(ParseTree tree) {
        return TerminalNode.class.isAssignableFrom(tree.getClass())
                && ".".equals(tree.getText());
    }

}
