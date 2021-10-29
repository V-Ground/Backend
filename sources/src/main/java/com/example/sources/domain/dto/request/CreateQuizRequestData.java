package com.example.sources.domain.dto.request;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizRequestData {
    private Long referenceId;
    private String question;
    private String description;
    private String answer;
    private Integer score;
}
