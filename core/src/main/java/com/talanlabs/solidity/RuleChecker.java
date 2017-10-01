package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationError;
import com.talanlabs.solidity.model.ValidationErrorCriticity;
import com.talanlabs.solidity.model.ValidationResults;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public abstract class RuleChecker extends SolidityBaseVisitor<ValidationResults> {
    private final ValidationResults results;

    public RuleChecker() {
        results = new ValidationResults();
    }

    public abstract String getRuleCode();
    public abstract String getRuleDescription();
    public abstract ValidationErrorCriticity getRuleCriticity();

    @Override
    public ValidationResults visitSourceUnit(SolidityParser.SourceUnitContext ctx) {
        super.visitSourceUnit(ctx);
        return results;
    }

    protected void addValidationError(ValidationError error) {
        results.addValidationError(error);
    }

    protected boolean isExpression(ParseTree tree) {
        return SolidityParser.ExpressionContext.class.isAssignableFrom(tree.getClass());
    }

    protected boolean isIdentifier(String expectedIdentifier, ParseTree tree) {
        return SolidityParser.IdentifierContext.class.isAssignableFrom(tree.getClass())
                && expectedIdentifier.equals(tree.getText());
    }

    protected boolean isTerminalNode(String expectedIdentifier, ParseTree tree) {
        return TerminalNode.class.isAssignableFrom(tree.getClass())
                && expectedIdentifier.equals(tree.getText());
    }

    protected boolean isVariableAssignedTo(SolidityParser.ExpressionContext ctx, String value) {
        return ctx.children.size() == 3
                && isTerminalNode("=", ctx.children.get(1))
                && value.equals(ctx.children.get(2).getText());
    }

    protected SolidityParser.IfStatementContext inIfClause(SolidityParser.ThrowStatementContext ctx) {
        SolidityParser.IfStatementContext ifStatementContext = null;
        ParserRuleContext parent = ctx;
        while (parent.getParent() != null) {
           parent = parent.getParent();
           if (SolidityParser.IfStatementContext.class.isAssignableFrom(parent.getClass()))
               ifStatementContext = (SolidityParser.IfStatementContext) parent;
        }
        return ifStatementContext;
    }

}
