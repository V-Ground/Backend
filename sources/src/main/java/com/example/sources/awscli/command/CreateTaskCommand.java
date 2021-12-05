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
        String responseJson = bashExecutor.execute(commandString);
        String taskArn = cliResponseParser.findTaskArn(responseJson);

        try { // ECS 컨테이너 생성되는 시간동안 잠시 대기
            Thread.sleep(5000);
        } catch (Exception e) {}

        return taskArn;
    }
}
