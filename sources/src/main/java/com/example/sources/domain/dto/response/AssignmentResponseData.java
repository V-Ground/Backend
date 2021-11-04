package com.example.sources.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseData {
    private Long assignmentId;
    private String title;
    private String description;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
