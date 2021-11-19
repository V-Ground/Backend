package com.example.sources.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class BashExecutor {
    private ProcessBuilder bash;

    public BashExecutor() {
        this.bash = new ProcessBuilder();
    }

    public String execute(String command, boolean isDebug) {
        String[] commands = command.split(" ");
        bash.command(commands);

        StringBuilder sb = new StringBuilder();
        BufferedReader outBuffer;
        try {
            Process process = bash.start();
            InputStream stream = isDebug ? process.getErrorStream() : process.getInputStream();
            outBuffer = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = outBuffer.readLine()) != null) {
                sb.append(line);
            }

            outBuffer.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public List<String> getNowExecuteCommand() {
        return bash.command();
    }
}
