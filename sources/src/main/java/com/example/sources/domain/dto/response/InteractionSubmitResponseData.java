package com.example.sources.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InteractionSubmitResponseData {
    private Long interactionSubmitId;
    private boolean yesNo;
    private String answer;
    private String studentName;

    public boolean getYesNo() {
        return yesNo;
    }
}
