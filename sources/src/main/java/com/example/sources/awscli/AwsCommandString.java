package com.example.sources.awscli;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsCommandString {

    @Value("${aws.ecs.cluster_name}")
    private String CLUSTER_NAME;
    @Value("${aws.ecs.subnet}")
    private String SUBNET;
    @Value("${aws.ecs.security_group}")
    private String SECURITY_GROUP;
    @Value("${aws.ecs.task_definition}")
    private String TASK_DEFINITION;

    /**
     * 생성된 ecs task 의 세부 정보를 조회한다.
     *
     * @param taskName ecs task
     * @return task detail
     */
    public String getTaskDetailCommand(String taskName) {
        return String.format("aws ecs describe-tasks " +
                "--cluster %s " +
                "--tasks %s", CLUSTER_NAME, taskName);
    }

    /**
     * ecs container 의 IP 를 반환한다.
     *
     * @param networkInterfaceId
     * @return 컨테이너의 networkIfsDetail
     */
    public String getNetworkIfsDetailCommand(String networkInterfaceId) {
        return String.format("aws ec2 describe-network-interfaces " +
                "--network-interface-ids %s", networkInterfaceId);
    }

    /**
     * ecs 컨테이너를 생성한다.
     *
     * @return 생성된 ecs 컨테이너의 id
     */
    public String createTaskCommand() {
        System.out.println("CLUSTER_NAME = " + CLUSTER_NAME);

        return String.format("aws ecs run-task " +
                "--cluster %s " +
                "--task-definition %s " +
                "--count 1 " +
                "--launch-type FARGATE --platform-version LATEST " +
                "--network-configuration=awsvpcConfiguration={subnets=%s,securityGroups=%s,assignPublicIp=ENABLED}",
                CLUSTER_NAME, TASK_DEFINITION, SUBNET, SECURITY_GROUP);
    }
}
