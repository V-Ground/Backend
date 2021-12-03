package com.example.sources.awscli;

import org.springframework.beans.factory.annotation.Value;

public class AwsCommandString {

    @Value("${aws.ecs.cluster_name}")
    private String CLUSTER_NAME = "KT-20211113-2";

    @Value("${aws.ecs.subnet}")
    private String SUBNET = "subnet-07439ed188223bb3f";

    @Value("${aws.ecs.security_group}")
    private String SECURITY_GROUP = "sg-01a19961262ae4372";

    public String getTaskDetailCommand(String taskName) {
        return String.format("aws ecs describe-tasks " +
                "--cluster %s " +
                "--tasks %s", CLUSTER_NAME, taskName);
    }

    public String getIpCommand(String networkInterfaceId) {
        return String.format("aws ec2 describe-network-interfaces " +
                "--network-interface-ids %s", networkInterfaceId);
    }

    public String createTaskCommand() {
        return String.format("aws ecs run-task " +
                "--cluster %s " +
                "--task-definition first-run-task-definition " +
                "--count 1 " +
                "--launch-type FARGATE --platform-version LATEST " +
                "--network-configuration=awsvpcConfiguration={subnets=%s,securityGroups=%s,assignPublicIp=ENABLED}",
                CLUSTER_NAME, SUBNET, SECURITY_GROUP);
    }
}
