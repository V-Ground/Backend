package com.example.sources.util;

import com.example.sources.awscli.AwsCliExecutor;
import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.AwsCommandString;
import com.example.sources.awscli.BashExecutor;
import com.example.sources.awscli.command.AwsCommand;
import com.example.sources.awscli.command.CreateTaskCommand;
import com.example.sources.awscli.command.GetIpCommand;
import com.example.sources.awscli.command.GetTaskDetailCommand;
import org.springframework.stereotype.Component;

@Component
public class AwsEcsUtil {

    private AwsCliExecutor awsCliExecutor;
    private AwsCommandString awsCommandString;

    public AwsEcsUtil() {
        BashExecutor bashExecutor = new BashExecutor();
        awsCommandString = new AwsCommandString();
        AwsCliResponseParser cliResponseParser = new AwsCliResponseParser();

        AwsCommand createTaskCommand = new CreateTaskCommand(bashExecutor, cliResponseParser);
        AwsCommand getTaskDetailCommand = new GetTaskDetailCommand(bashExecutor, cliResponseParser);
        AwsCommand getIpCommand = new GetIpCommand(bashExecutor, cliResponseParser);

        awsCliExecutor = new AwsCliExecutor(
                createTaskCommand,
                getTaskDetailCommand,
                getIpCommand);
    }

    public String createEcsContainer() {
        String task = awsCliExecutor.createTask(awsCommandString.createTaskCommand());
        String eid = awsCliExecutor.getTaskDetail(awsCommandString.getTaskDetailCommand(task));

        return awsCliExecutor.getIp(awsCommandString.getIpCommand(eid));
    }
}
