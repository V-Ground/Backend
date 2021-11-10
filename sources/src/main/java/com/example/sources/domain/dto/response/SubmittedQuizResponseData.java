package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmittedQuizResponseData {
    private Long answerId;
    private String question;
    private String submittedAnswer;
    private Integer scored;
}
