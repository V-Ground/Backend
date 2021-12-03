package com.example.sources.awscli.command;

import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.BashExecutor;

public class GetIpCommand implements AwsCommand{
    private BashExecutor bashExecutor;
    private AwsCliResponseParser cliResponseParser;

    public GetIpCommand(BashExecutor bashExecutor,
                        AwsCliResponseParser cliResponseParser) {
        this.bashExecutor = bashExecutor;
        this.cliResponseParser = cliResponseParser;
    }

    @Override
    public String execute(String commandString) {
        String responseJson = bashExecutor.execute(commandString, false);
        String ip = cliResponseParser.findIp(responseJson);

        return ip;
    }
}
