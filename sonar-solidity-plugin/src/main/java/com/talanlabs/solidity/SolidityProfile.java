package com.talanlabs.solidity;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

import static com.talanlabs.solidity.SolidityPlugin.LANGUAGE_KEY;
import static com.talanlabs.solidity.SolidityRulesDefinition.REPOSITORY_KEY;

public
class SolidityProfile extends ProfileDefinition {
    public static final String PROFILE_NAME = "Default";
    private final RuleFinder ruleFinder;

    public SolidityProfile(RuleFinder ruleFinder) {
        this.ruleFinder = ruleFinder;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages validationMessages) {
        RulesProfile profile = RulesProfile.create(PROFILE_NAME, LANGUAGE_KEY);
        activateRulesFromDefaultProfile(profile);
        return profile;
    }

    private void activateRulesFromDefaultProfile(RulesProfile profile) {
        for (RuleChecker check : SolidityRulesDefinition.rules) {
            Rule rule = ruleFinder.findByKey(REPOSITORY_KEY, check.getRuleCode());
            profile.activateRule(rule, null);
        }
    }
}
