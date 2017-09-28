package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationError;
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

    @Override
    public ValidationResults visitSourceUnit(SolidityParser.SourceUnitContext ctx) {
        super.visitSourceUnit(ctx);
        return results;
    }

    protected void addValidationError(ValidationError error) {
        results.addValidationError(error);
    }

    protected Token min(Token token1, Token token2) {
        if (token1.getLine() > token2.getLine())
            return token2;
        else if (token1.getLine() < token2.getLine())
            return token1;
        else if (token1.getCharPositionInLine() <= token2.getCharPositionInLine())
            return token1;
        else
            return token2;
    }

    protected Token max(Token token1, Token token2) {
        if (token1.getLine() > token2.getLine())
            return token1;
        else if (token1.getLine() < token2.getLine())
            return token2;
        else if (token1.getCharPositionInLine() >= token2.getCharPositionInLine())
            return token1;
        else
            return token2;
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

    protected boolean isComparedToSomething(SolidityParser.ExpressionContext ctx) {
        ParserRuleContext parent = ctx.getParent();
        boolean isParentAnExpression = SolidityParser.ExpressionContext.class.isAssignableFrom(parent.getClass());
        List<ParseTree> siblings = parent.children;
        boolean comparedToSomething = siblings.stream()
                .anyMatch(parseTree ->
                        TerminalNode.class.isAssignableFrom(parseTree.getClass())
                                && "==".equals(parseTree.getText())
                );
        return isParentAnExpression && siblings.size() > 2 && comparedToSomething;
    }

}
