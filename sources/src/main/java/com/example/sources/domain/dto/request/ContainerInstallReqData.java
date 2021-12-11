package com.example.sources.domain.dto.request;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerInstallReqData extends ContainerBaseField {
    private String programName;
}
