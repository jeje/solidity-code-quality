package com.talanlabs.solidity;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

import static org.junit.Assert.assertEquals;

public class SolidityRulesDefinitionTest {

    @Test
    public void test_definition() {
        RulesDefinition.Context context = new RulesDefinition.Context();
        SolidityRulesDefinition rulesDefinition = new SolidityRulesDefinition();
        rulesDefinition.define(context);
        assertEquals(1, context.repositories().size());
        RulesDefinition.Repository repository = context.repositories().get(0);
        assertEquals(new RulesRepository().getRules().size(), repository.rules().size());
    }

}
