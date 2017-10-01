package com.talanlabs.solidity;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner();
        Options options = new Options();
        final CmdLineParser parser = new CmdLineParser(options);
        if (args.length < 1) {
            parser.printUsage(System.out);
            System.exit(-1);
        }
        try {
            parser.parseArgument(args);
            scanner.scan(options.getContract());
        } catch (CmdLineException clEx) {
            System.err.println("ERROR: invalid arguments");
            parser.printUsage(System.out);
        }
    }
}
