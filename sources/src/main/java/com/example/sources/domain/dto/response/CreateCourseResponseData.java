package com.example.sources.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseResponseData {
    private String title;
    private String description;
    private String cpu;
    private String ram;
    private Long osType;
}
