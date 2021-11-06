package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponseData {
    private Long quizId;
    private String question;
    private String description;
    private Integer score;
}
