package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseResponseData {
    private Long courseId;
    private String title;
    private String description;
    private String cpu;
    private String memory;
    private String os;
}
