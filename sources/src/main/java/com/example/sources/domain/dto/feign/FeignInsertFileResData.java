package com.example.sources.domain.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeignInsertFileResData {
    private boolean status;
    private String filename;
    private String savedPath;

    public boolean getStatus() {
        return status;
    }
}
