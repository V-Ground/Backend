package com.example.sources.awscli.command;

import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.BashExecutor;

public class GetLastStatusCommand implements AwsCommand {
    private BashExecutor bashExecutor;
    private AwsCliResponseParser cliResponseParser;

    public GetLastStatusCommand(BashExecutor bashExecutor, AwsCliResponseParser cliResponseParser) {
        this.bashExecutor = bashExecutor;
        this.cliResponseParser = cliResponseParser;
    }

    @Override
    public String execute(String commandString) {
        String taskDetail = bashExecutor.execute(commandString);
        String lastStatus = cliResponseParser.findLastStatus(taskDetail);
        System.out.println("lastStatus = " + lastStatus);

        return lastStatus;
    }
}
