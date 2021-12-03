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
        Process process = null;
        try {
            process = bash.start();
            InputStream inputStream = isDebug ? process.getErrorStream() : process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(process != null) { // 할당 해제
                process.destroy();
            }
        }

        return sb.toString();
    }
}
