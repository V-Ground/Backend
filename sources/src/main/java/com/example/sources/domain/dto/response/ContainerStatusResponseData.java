package com.example.sources.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContainerStatusResponseData {
    private Long studentId;
    private String studentName;
    private boolean status;

    public boolean getStatus() {
        return status;
    }
}
