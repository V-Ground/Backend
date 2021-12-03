package com.example.sources.awscli.command;

import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.BashExecutor;

public class GetNIDCommand implements AwsCommand{
    private BashExecutor bashExecutor;
    private AwsCliResponseParser cliResponseParser;

    public GetNIDCommand(BashExecutor bashExecutor, AwsCliResponseParser cliResponseParser) {
        this.bashExecutor = bashExecutor;
        this.cliResponseParser = cliResponseParser;
    }

    @Override
    public String execute(String commandString) {
        String taskDetail = bashExecutor.execute(commandString, false);
        String networkInterfaceId = cliResponseParser.findNetworkInterfaceId(taskDetail);

        try {
            Thread.sleep(4000);
        } catch (Exception e) {}

        return networkInterfaceId;
    }
}
