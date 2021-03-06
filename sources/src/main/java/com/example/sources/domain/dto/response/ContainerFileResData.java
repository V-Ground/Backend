package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerFileResData {
    private Long studentId;
    private boolean status;
    private String fileContent;
    private boolean error = false;

    public boolean getError() {
        return error;
    }
    public boolean getStatus() {
        return status;
    }
}
