package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationResults;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

import static com.talanlabs.solidity.SolidityParser.*;

public class SolidityParseTreeVisitor extends SolidityBaseVisitor<ValidationResults> {

    @Override
    public ValidationResults visitSourceUnit(SourceUnitContext ctx) {
        super.visitSourceUnit(ctx);
        return new ValidationResults();
    }

    @Override
    public ValidationResults visitContractDefinition(ContractDefinitionContext ctx) {
        IdentifierContext id = ctx.identifier();
        System.out.println("Analyzed contract '" + id.getText() + "'");
        return super.visitContractDefinition(ctx);
    }

    @Override
    public ValidationResults visitEventDefinition(EventDefinitionContext ctx) {
        IdentifierContext identifier = ctx.identifier();
        List<IndexedParameterContext> parameters = ctx.indexedParameterList().indexedParameter();
        System.out.println("Analyzed event '" + identifier.getText() + "'");
        for (int i = 0; i < parameters.size(); i++) {
            IndexedParameterContext parameter = parameters.get(i);
            ParseTree parameterType = parameter.typeName().elementaryTypeName().getChild(0);
            System.out.println("\tparameter " + parameter.identifier().getText() + " of type " + parameterType.toString());
        }
        return super.visitEventDefinition(ctx);
    }

    @Override
    public ValidationResults visitThrowStatement(ThrowStatementContext ctx) {
        System.out.println("Found throw statement!");
        return super.visitThrowStatement(ctx);
    }
}
