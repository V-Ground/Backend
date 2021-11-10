package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWithSubmittedResponseData {
    private AssignmentDetailResponseData questionDetail;
    private List<SubmittedQuestionResponseData> submittedAnswerDetail;
}
