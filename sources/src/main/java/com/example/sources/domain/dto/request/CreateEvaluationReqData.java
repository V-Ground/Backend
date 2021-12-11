package com.example.sources.domain.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEvaluationReqData {
    private Long userId;
    private String title;
    private String description;
    private String cpu;
    private String memory;
    private String os;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
