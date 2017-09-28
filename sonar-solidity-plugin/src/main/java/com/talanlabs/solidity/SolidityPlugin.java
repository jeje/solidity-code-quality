package com.talanlabs.solidity;

import org.sonar.api.Plugin;

public class SolidityPlugin implements Plugin {
    public static final String LANGUAGE_KEY = "solidity";
    public static final String LANGUAGE_NAME = "Solidity";

    @Override
    public void define(Context context) {
        context
                .addExtension(SolidityLanguage.class)
                .addExtension(SolidityProfile.class)
                .addExtension(SolidityRulesDefinition.class)
                .addExtension(SoliditySensor.class);
    }
}