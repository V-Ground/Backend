package com.example.sources.domain.dto.response;

import com.example.sources.domain.dto.request.ContainerBaseField;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerFileInsertResData {
    private Long studentId;
    private boolean status;
    private String filename;
    private String savedPath;
    private boolean error;

    public boolean getStatus() {
        return status;
    }
}
