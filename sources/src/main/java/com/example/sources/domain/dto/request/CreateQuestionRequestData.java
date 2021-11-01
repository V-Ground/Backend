package com.example.sources.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequestData {
    private String question;
    private String description;
    private String answer;
    private Integer score;
}
