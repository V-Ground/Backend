package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDetailResponseData {
    private AssignmentResponseData assignment;
    private List<QuestionResponseData> questions;
}
