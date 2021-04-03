package io.wijaya.ceres.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "ceres")
public class Root implements Runnable {
    
    @Parameters(paramLabel = "<words>", defaultValue = "Welcome to Ceres")
    private String[] params = {};

    @Override
    public void run(){
    }

}
