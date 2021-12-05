package com.example.sources.util;

import com.example.sources.awscli.AwsCliExecutor;
import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.AwsCommandString;
import com.example.sources.awscli.BashExecutor;
import com.example.sources.awscli.command.AwsCommand;
import com.example.sources.awscli.command.CreateTaskCommand;
import com.example.sources.awscli.command.GetIpCommand;
import com.example.sources.awscli.command.GetNIDCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AwsEcsUtil {

    private AwsCliExecutor awsCliExecutor;
    private AwsCommandString awsCommandString;

    public AwsEcsUtil(AwsCommandString awsCommandString) {
        this.awsCommandString = awsCommandString;
        BashExecutor bashExecutor = new BashExecutor();
        AwsCliResponseParser cliResponseParser = new AwsCliResponseParser();

        AwsCommand createTaskCommand = new CreateTaskCommand(bashExecutor, cliResponseParser);
        AwsCommand getTaskDetailCommand = new GetNIDCommand(bashExecutor, cliResponseParser);
        AwsCommand getIpCommand = new GetIpCommand(bashExecutor, cliResponseParser);

        awsCliExecutor = new AwsCliExecutor(
                createTaskCommand,
                getTaskDetailCommand,
                getIpCommand);
    }

    /**
     * aws cli 를 이용하여 ecs 컨테이너를 생성한다.
     *
     * @return 생성된 컨테이너의 public ip
     */
    public String createEcsContainer() {
        String task = awsCliExecutor.createTask(awsCommandString.createTaskCommand());
        String networkInterfaceId = awsCliExecutor.getNetworkIfs(awsCommandString.getTaskDetailCommand(task));

        return awsCliExecutor.getIp(awsCommandString.getNetworkIfsDetailCommand(networkInterfaceId));
    }
}
