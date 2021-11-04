package com.example.sources.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDetailResponseData {
    private AssignmentResponseData assignment;
    private List<QuestionResponseData> questions;
}
