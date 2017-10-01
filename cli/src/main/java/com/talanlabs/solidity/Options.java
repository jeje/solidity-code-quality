package com.talanlabs.solidity;

import org.kohsuke.args4j.Option;

public class Options {
    @Option(name = "-f", usage = "Solidity contract to analyze", required = true)
    private String contract;

    public String getContract() {
        return contract;
    }
}
