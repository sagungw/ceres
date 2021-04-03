package io.wijaya.ceres;

import io.wijaya.ceres.cli.Root;
import io.wijaya.ceres.cli.Vision;
import picocli.CommandLine;

public class App {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Root()).addSubcommand(new Vision()).execute(args);
        // System.exit(exitCode);  
    }
}
