package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSummaryResData {

    private List<QuestionResponseData> questions;
    private List<StudentSubmittedQuestionResData> students;
}
