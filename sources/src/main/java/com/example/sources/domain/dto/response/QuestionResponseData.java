package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseData {
    private Long questionId;
    private String question;
    private String description;
    private Integer score;
}
