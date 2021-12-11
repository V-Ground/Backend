package com.example.sources.domain.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusResData {
    private boolean status;
    private boolean error = false;

    public StatusResData(boolean error) {
        this.error = error;
    }

    public boolean getStatus() {
        return status;
    }

    public boolean getError() { return error; }
}
