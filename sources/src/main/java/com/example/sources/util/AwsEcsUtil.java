package com.example.sources.util;

import com.example.sources.awscli.AwsCliExecutor;
import com.example.sources.awscli.AwsCliResponseParser;
import com.example.sources.awscli.AwsCommandString;
import com.example.sources.awscli.BashExecutor;
import com.example.sources.awscli.command.*;
import com.example.sources.domain.dto.aws.TaskInfo;
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
        AwsCommand getLastStatus = new GetLastStatusCommand(bashExecutor, cliResponseParser);

        awsCliExecutor = new AwsCliExecutor(
                createTaskCommand,
                getTaskDetailCommand,
                getIpCommand,
                getLastStatus);
    }

    /**
     * aws cli 를 이용하여 ecs 컨테이너를 생성한다.
     *
     * @return 생성된 컨테이너의 public ip
     */
    public TaskInfo createEcsContainer() {
        String taskArn = awsCliExecutor.createTask(awsCommandString.createTaskCommand());
        String networkInterfaceId = awsCliExecutor.getNetworkIfs(awsCommandString.getTaskDetailCommand(taskArn));
        String ip = awsCliExecutor.getIp(awsCommandString.getNetworkIfsDetailCommand(networkInterfaceId));

        return new TaskInfo(taskArn, ip);
    }

    /**
     * aws cli 를 이용하여 컨테이너의 상태를 확인한다.
     *
     * @param taskArn 조회할 컨테이너의 ARN
     * @return 컨테이너의 상태 | RUNNING | PENDING | STOPPED | PROVISIONING
     */
    public String getTaskStatus(String taskArn) {
        return awsCliExecutor.getLastStatus(awsCommandString.getTaskDetailCommand(taskArn));
    }
}
