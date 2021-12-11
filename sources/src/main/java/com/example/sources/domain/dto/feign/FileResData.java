package com.example.sources.domain.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResData {
    private boolean status;
    private List<String> fileContent;

    public boolean getStatus() {
        return status;
    }
}
