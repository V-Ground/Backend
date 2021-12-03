package com.example.sources.awscli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BashExecutor {
    private ProcessBuilder bash;

    public BashExecutor() {
        this.bash = new ProcessBuilder();
    }

    /**
     * bash 를 실행시킨다
     *
     * @param command 실행 시킬 명령어
     * @param isDebug 디버그모드
     * @return 실행시킨 bash command 의 result
     */
    public String execute(String command, boolean isDebug) {
        String[] commands = command.split(" ");
        bash.command(commands);

        StringBuilder sb = new StringBuilder();
        InputStream inputStream;
        BufferedReader outBuffer;
        try {
            Process process = bash.start();
            inputStream = isDebug ? process.getErrorStream() : process.getInputStream();
            outBuffer = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = outBuffer.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();
            outBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
