package com.talanlabs.solidity;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.junit.Assert.assertEquals;

public class SolidityPluginTest {

    @Test
    public void test_plugin_definition() {
        Plugin.Context context = new Plugin.Context(SonarRuntimeImpl.forSonarQube(Version.parse("6.5"), SonarQubeSide.SERVER));
        SolidityPlugin plugin = new SolidityPlugin();
        plugin.define(context);
        assertEquals(4, context.getExtensions().size());
    }

}
