package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionSubmitResponseData {
    private Long interactionId;
    private Long interactionSubmitId;
    private boolean yesNo;
    private String answer;
    private String studentName;

    public boolean getYesNo() {
        return yesNo;
    }
}
