package com.example.sources.awscli;

import com.example.sources.domain.dto.aws.TaskDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class AwsCliResponseParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String findTaskArn(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json).at("/tasks/0/containers/0/taskArn");
            return jsonNode.toString().replaceAll("\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String findEid(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json).findValue("details");

            List<TaskDetail> taskDetails = objectMapper
                    .readValue(jsonNode.toString(), new TypeReference<List<TaskDetail>>() {});

            for (TaskDetail taskDetail : taskDetails) {
                if(taskDetail.getName().equals("networkInterfaceId")) {
                    return taskDetail.getValue();
                }
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findIp(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json).at("/NetworkInterfaces/0/Association/PublicIp");
            return jsonNode.toString().replaceAll("\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
