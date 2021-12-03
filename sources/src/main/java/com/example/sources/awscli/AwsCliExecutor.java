package com.example.sources.awscli;

import com.example.sources.awscli.command.AwsCommand;

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

    public String createTask(String commandString) {
        return createTaskCommand.execute(commandString);
    }

    public String getTaskDetail(String commandString) {
        return getTaskDetailCommand.execute(commandString);
    }

    public String getIp(String commandString) {
        return getIpCommand.execute(commandString);
    }
}
