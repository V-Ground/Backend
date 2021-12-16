package com.example.sources.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubmittedInteractionResData {
    private Long studentId;
    private List<InteractionSubmitResponseData> submittedInteractions;
}
