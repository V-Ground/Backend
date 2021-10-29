package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyParticipatingResponseData {
    private List<CourseResponseData> course;
    private List<EvaluationResponseData> evaluation;
}
