package com.example.sources.awscli;

import com.example.sources.awscli.command.AwsCommand;
import org.springframework.stereotype.Component;

public class AwsCliExecutor {
    private AwsCommand createTaskCommand;
    private AwsCommand getTaskDetailCommand;
    private AwsCommand getIpCommand;

    public AwsCliExecutor(AwsCommand createTaskCommand,
                          AwsCommand getTaskDetailCommand,
                          AwsCommand getIpCommand) {
        this.createTaskCommand = createTaskCommand;
        this.getTaskDetailCommand = getTaskDetailCommand;
        this.getIpCommand = getIpCommand;
    }

    /**
     * commandString 을 받아 ecs container 를 생성하는 aws cli 를 호출한다.
     *
     * @param commandString 실행시킬 aws cli command
     * @return 생성된 ecs container 의 arn
     */
    public String createTask(String commandString) {
        return createTaskCommand.execute(commandString);
    }

    /**
     * taskArn 을 받아 networkInterface 을 조회하는 aws cli 를 호출한다.
     *
     * @param commandString taskArn 을 조회하는 commandString
     * @return taskArn
     */
    public String getNetworkIfs(String commandString) {
        return getTaskDetailCommand.execute(commandString);
    }

    /**
     * commandString 을 받아 ip 를 조회하는 aws cli 를 호출한다.
     *
     * @param commandString networkInterfaceId 를 조회하는
     * @return container ip
     */
    public String getIp(String commandString) {
        return getIpCommand.execute(commandString);
    }
}
