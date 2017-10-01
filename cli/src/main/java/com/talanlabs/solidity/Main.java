package com.talanlabs.solidity;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;

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
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("ERROR: can't read contract " + options.getContract());
            System.exit(-1);
        }
    }
}
