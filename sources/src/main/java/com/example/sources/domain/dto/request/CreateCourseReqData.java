package com.example.sources.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseReqData {
    private Long userId;
    private String title;
    private String description;
    private String cpu;
    private String memory;
    private String os;
}
