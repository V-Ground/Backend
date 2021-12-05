package com.example.sources.awscli.command;

import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.BashExecutor;

public class RemoteExecuteCommand implements AwsCommand {

    private BashExecutor bashExecutor;
    private AwsCliResponseParser cliResponseParser;

    public RemoteExecuteCommand(BashExecutor bashExecutor, AwsCliResponseParser cliResponseParser) {
        this.bashExecutor = bashExecutor;
        this.cliResponseParser = cliResponseParser;
    }

    @Override
    public String execute(String commandString) {
        return null;
    }
}
