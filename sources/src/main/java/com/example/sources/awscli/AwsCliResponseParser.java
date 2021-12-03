package com.example.sources.awscli;

import com.example.sources.domain.dto.aws.TaskDetail;
import com.example.sources.exception.AwsResponseParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class AwsCliResponseParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 생성된 task 로부터 받은 json 을 토대로 task 의 Arn 을 파싱한다.
     *
     * @param taskDetail taskDetail
     * @return taskArn
     */
    public String findTaskArn(String taskDetail) {
        try {
            JsonNode jsonNode = objectMapper.readTree(taskDetail).at("/tasks/0/containers/0/taskArn");
            return jsonNode.toString().replaceAll("\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        throw new AwsResponseParseException("taskArn 을 찾을 수 없습니다.");
    }

    /**
     * taskDetail 을 토대로 networkInterfaceId 를 파싱한다
     *
     * @param taskDetail taskArn
     * @return networkInterfaceId
     */
    public String findNetworkInterfaceId(String taskDetail) {
        try {
            JsonNode jsonNode = objectMapper.readTree(taskDetail).findValue("details");

            List<TaskDetail> taskDetails = objectMapper
                    .readValue(jsonNode.toString(), new TypeReference<List<TaskDetail>>() {});

            for (TaskDetail iter : taskDetails) {
                if(iter.getName().equals("networkInterfaceId")) {
                    return iter.getValue();
                }
            }
            throw new AwsResponseParseException("networkInterfaceId 를 찾을 수 없습니다.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new AwsResponseParseException("networkInterfaceId 를 찾을 수 없습니다.");
    }

    /**
     * networkInterface 를 토대로 public IP 를 파싱한다.
     *
     * @param networkInterface networkInterface
     * @return public IP
     */
    public String findIp(String networkInterface) {
        try {
            JsonNode jsonNode = objectMapper.readTree(networkInterface).at("/NetworkInterfaces/0/Association/PublicIp");
            return jsonNode.toString().replaceAll("\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new AwsResponseParseException("ip 를 찾을 수 없습니다.");
    }
}
