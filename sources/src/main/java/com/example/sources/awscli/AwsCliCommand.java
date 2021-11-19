package com.example.sources.awscli;

import org.springframework.stereotype.Component;

@Component
public class AwsCliCommand {
    public String getTaskDetailCommand(String clusterName, String taskName) {
        return String.format("aws ecs describe-tasks " +
                "--cluster %s " +
                "--tasks %s", clusterName, taskName);
    }

    public String getNetworkInterfaceCommand(String networkInterfaceId) {
        return String.format("aws ec2 describe-network-interfaces " +
                "--network-interface-ids %s", networkInterfaceId);
    }

    public String createTaskCommand(String clusterName) {
        return String.format("aws ecs run-task " +
                        "--cluster %s " +
                        "--task-definition first-run-task-definition " +
                        "--count 1 " +
                        "--launch-type FARGATE --platform-version LATEST " +
                        "--network-configuration=awsvpcConfiguration={subnets=subnet-07439ed188223bb3f,securityGroups=sg-01a19961262ae4372,assignPublicIp=ENABLED}",
                clusterName);
    }
}
