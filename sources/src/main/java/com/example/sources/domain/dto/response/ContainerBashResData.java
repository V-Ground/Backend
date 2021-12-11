package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerBashResData {
    private Long studentId;
    private String commandResult;
    private boolean error = false;

    public boolean getError() {
        return error;
    }
}
