package com.example.sources.domain.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeignInstallResData {
    private boolean status;
    private String installPath;
    private String version;

    public boolean getStatus() {
        return status;
    }
}
