package com.example.sources.awscli.command;

import org.springframework.stereotype.Component;

public interface AwsCommand {

    /**
     * aws cli command 를 실행한다.
     *
     * @param commandString 실행할 command
     * @return command 의 result 문자열
     */
    String execute(String commandString);
}
