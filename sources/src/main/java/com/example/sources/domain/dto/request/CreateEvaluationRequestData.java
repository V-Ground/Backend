package com.example.sources.domain.dto.request;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEvaluationRequestData {
    private Long userId;
    private String title;
    private String description;
    private String cpu;
    private String ram;
    private Long osType;
}
