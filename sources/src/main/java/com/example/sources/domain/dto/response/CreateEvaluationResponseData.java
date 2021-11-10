package com.example.sources.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEvaluationResponseData {
    private String title;
    private String description;
    private String cpu;
    private String memory;
    private String os;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
