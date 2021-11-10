package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDetailResponseData {
    private EvaluationResponseData evaluation;
    private List<QuizResponseData> quizzes;
}
