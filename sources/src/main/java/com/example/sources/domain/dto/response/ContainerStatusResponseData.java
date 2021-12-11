package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerStatusResponseData {
    private Long studentId;
    private String studentName;
    private boolean status;
    private boolean error;

    public boolean getStatus() {
        return status;
    }

    public boolean getError() { return error; }
}