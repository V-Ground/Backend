package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerActivationResData {
    private Long studentId;
    private boolean keyboardActivation;
    private boolean mouseActivation;
    private boolean error;

    public boolean getKeyboardActivation() {
        return keyboardActivation;
    }

    public boolean getMouseActivation() {
        return mouseActivation;
    }

    public boolean getError() {
        return error;
    }
}
