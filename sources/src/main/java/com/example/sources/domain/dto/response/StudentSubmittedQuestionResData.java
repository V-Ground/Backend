package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubmittedQuestionResData {

    private Long studentId;
    private List<SubmittedQuestionResponseData> submittedQuestions;

}
