package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEvaluationResponseData {
    private String title;
    private String description;
    private String cpu;
    private String ram;
    private Long osType;
}
