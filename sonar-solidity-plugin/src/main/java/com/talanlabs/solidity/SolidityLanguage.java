package com.talanlabs.solidity;

import org.sonar.api.resources.AbstractLanguage;

import static com.talanlabs.solidity.SolidityPlugin.LANGUAGE_KEY;
import static com.talanlabs.solidity.SolidityPlugin.LANGUAGE_NAME;

public class SolidityLanguage extends AbstractLanguage {
    public SolidityLanguage() {
        super(LANGUAGE_KEY, LANGUAGE_NAME);
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[] { "sol "};
    }
}
