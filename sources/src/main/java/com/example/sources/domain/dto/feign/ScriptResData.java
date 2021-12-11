package com.example.sources.domain.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScriptResData extends BashResponseData {
    private boolean status;
    private String savedPath;
    private String commandResult;
}
