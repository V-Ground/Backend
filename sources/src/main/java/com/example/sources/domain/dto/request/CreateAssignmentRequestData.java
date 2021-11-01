package com.example.sources.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentRequestData {
    private String title;
    private String description;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
