package com.example.sources.domain.dto.request;

import com.example.sources.domain.type.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInteractionReqData {
    private String title;
    private InteractionType interactionType;
}
