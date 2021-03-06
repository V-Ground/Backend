package com.example.sources.domain.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeignBashResponseData {
    private String commandResult;
    private boolean error = false;

    public FeignBashResponseData(boolean error) {
        this.error = error;
    }

    public boolean getError() {
        return error;
    }
}
