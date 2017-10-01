package com.talanlabs.solidity;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.ValidationMessages;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SolidityProfileTest {

    @Test
    public void test_definition() {
        SolidityRulesDefinition rules = new SolidityRulesDefinition();
        RulesDefinition.Context rulesDefinitionContext = new RulesDefinition.Context();
        rules.define(rulesDefinitionContext);
        assertEquals(1, rulesDefinitionContext.repositories().size());
        RulesDefinition.Repository repository = rulesDefinitionContext.repositories().get(0);
        RuleFinder ruleFinder = new StubbedRuleFinder(repository.rules());
        SolidityProfile solidityProfile = new SolidityProfile(ruleFinder);
        ValidationMessages validationMessages = ValidationMessages.create();
        RulesProfile profile = solidityProfile.createProfile(validationMessages);
        // all rules should be active by default
        assertEquals(new RulesRepository().getRules().size(), profile.getActiveRules().size());
    }

    class StubbedRuleFinder implements RuleFinder {
        private final List<RulesDefinition.Rule> rules;

        StubbedRuleFinder(List<RulesDefinition.Rule> rules) {
            this.rules = rules;
        }

        @Override
        public Rule findById(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Rule findByKey(String repositoryKey, String key) {
            return rules.stream()
                    .filter(r -> r.key().equals(key) && r.repository().key().equals(repositoryKey))
                    .map(r -> Rule.create(r.repository().key(), r.key(), r.name()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Rule findByKey(RuleKey ruleKey) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Rule find(RuleQuery ruleQuery) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<Rule> findAll(RuleQuery ruleQuery) {
            throw new UnsupportedOperationException();
        }
    }

}
