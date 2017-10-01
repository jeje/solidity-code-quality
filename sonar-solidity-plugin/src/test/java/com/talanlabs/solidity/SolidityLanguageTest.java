package com.talanlabs.solidity;

import org.junit.Test;

import static com.talanlabs.solidity.SolidityPlugin.LANGUAGE_KEY;
import static com.talanlabs.solidity.SolidityPlugin.LANGUAGE_NAME;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SolidityLanguageTest {

    @Test
    public void test_definition() {
        SolidityLanguage language = new SolidityLanguage();
        assertEquals(LANGUAGE_KEY, language.getKey());
        assertEquals(LANGUAGE_NAME, language.getName());
        assertArrayEquals(new String[] {"sol"}, language.getFileSuffixes());
    }

}
