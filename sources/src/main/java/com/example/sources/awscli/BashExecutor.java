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

    public String execute(String command, boolean isDebug) {
        String[] commands = command.split(" ");
        bash.command(commands);

        StringBuilder sb = new StringBuilder();
        BufferedReader outBuffer;
        try {
            Process process = bash.start();
            InputStream inputStream = isDebug ? process.getErrorStream() : process.getInputStream();
            outBuffer = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = outBuffer.readLine()) != null) {
                sb.append(line);
            }
            process.waitFor();
            inputStream.close();
            outBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
