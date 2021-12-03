package com.example.sources.awscli.command;

import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.BashExecutor;

public class GetTaskDetailCommand implements AwsCommand{
    private BashExecutor bashExecutor;
    private AwsCliResponseParser cliResponseParser;

    public GetTaskDetailCommand(BashExecutor bashExecutor, AwsCliResponseParser cliResponseParser) {
        this.bashExecutor = bashExecutor;
        this.cliResponseParser = cliResponseParser;
    }

    @Override
    public String execute(String commandString) {
        String responseJson = bashExecutor.execute(commandString, false);
        String networkInterfaceId = cliResponseParser.findEid(responseJson);

        try {
            Thread.sleep(5000);
        } catch (Exception e) {}

        return networkInterfaceId;
    }
}
