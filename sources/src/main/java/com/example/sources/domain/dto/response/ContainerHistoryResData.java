package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerHistoryResData {
    private Long studentId;
    private String fileContent;
    private boolean error = false;

    public boolean getError() {
        return error;
    }
}
