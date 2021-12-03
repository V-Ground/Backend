package com.example.sources.awscli.command;

import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.BashExecutor;

public class CreateTaskCommand implements AwsCommand{
    private BashExecutor bashExecutor;
    private AwsCliResponseParser cliResponseParser;

    public CreateTaskCommand(BashExecutor bashExecutor,
                             AwsCliResponseParser cliResponseParser) {
        this.bashExecutor = bashExecutor;
        this.cliResponseParser = cliResponseParser;
    }

    @Override
    public String execute(String commandString) {
        String responseJson = bashExecutor.execute(commandString, false);
        String taskArn = cliResponseParser.findTaskArn(responseJson);

        try {
            Thread.sleep(5000);
        } catch (Exception e) {}

        return taskArn;
    }
}
