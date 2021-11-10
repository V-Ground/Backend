package com.example.sources.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseData {
    private Long evaluationId;
    private String title;
    private String description;
    private Boolean visibility;
    private String teacherName;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
