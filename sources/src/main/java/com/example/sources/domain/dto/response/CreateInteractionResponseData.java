package com.example.sources.domain.dto.response;

import com.example.sources.domain.type.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInteractionResponseData {
    private Long interactionId;
    private String title;
    private InteractionType interactionType;
    private LocalDateTime createdAt;
}
