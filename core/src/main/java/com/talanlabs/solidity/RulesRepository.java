package com.talanlabs.solidity;

import com.talanlabs.solidity.rules.ThrowDeprecatedRuleChecker;
import com.talanlabs.solidity.rules.TxOriginRuleChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RulesRepository {
    private List<RuleChecker> rules = new ArrayList<>();

    public RulesRepository() {
        registerRule(new ThrowDeprecatedRuleChecker());
        registerRule(new TxOriginRuleChecker());
    }

    private void registerRule(RuleChecker rule) {
        rules.add(rule);
    }

    public List<RuleChecker> getRules() {
        return Collections.unmodifiableList(rules);
    }
}
