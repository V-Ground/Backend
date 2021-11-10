package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmittedQuestionResponseData {
    private Long questionId;
    private String question;
    private String submittedAnswer;
    private Integer scored;
}
