package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerInstallResData {
    private Long studentId;
    private boolean status;
    private String installPath;
    private String version;
    private boolean error = false;

    public boolean getError() {
        return error;
    }

    public boolean getStatus() {
        return status;
    }
}
