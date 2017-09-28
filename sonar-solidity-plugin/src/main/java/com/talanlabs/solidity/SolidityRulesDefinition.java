package com.talanlabs.solidity;

import com.talanlabs.solidity.rules.ThrowDeprecatedRuleChecker;
import com.talanlabs.solidity.rules.TxOriginRuleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SolidityRulesDefinition implements RulesDefinition {
    public static final String REPOSITORY_KEY = "Solidity";
    public static final String REPOSITORY_NAME = "SonarAnalyzer";
    public static final List<RuleChecker> rules = Arrays.asList(
            new ThrowDeprecatedRuleChecker(),
            new TxOriginRuleChecker()
    );

    private static final Logger LOG = LoggerFactory.getLogger(SolidityRulesDefinition.class);

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(REPOSITORY_KEY, SolidityPlugin.LANGUAGE_KEY)
                .setName(REPOSITORY_NAME);
        for (RuleChecker rule : rules) {
            registerRule(repository, rule);
        }
        repository.done();
    }

    private void registerRule(NewRepository repository, RuleChecker checker) {
        LOG.debug("Registering rule '{}:{}'", REPOSITORY_KEY, checker.getRuleCode());
        repository.createRule(checker.getRuleCode())
                .setInternalKey(checker.getRuleCode())
                .setName(checker.getRuleDescription())
                .setHtmlDescription(checker.getRuleDescription())
                .setStatus(RuleStatus.READY)
                .setActivatedByDefault(true)
                .setSeverity(checker.getRuleCriticity().name())
                .setType(RuleType.VULNERABILITY);   // TODO: fix this!!
    }

}
