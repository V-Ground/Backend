package com.example.sources.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseData {
    private Long questionId;
    private String question;
    private String description;
    private Integer score;
}
