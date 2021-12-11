package com.example.sources.domain.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentReqData {
    private String title;
    private String description;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
