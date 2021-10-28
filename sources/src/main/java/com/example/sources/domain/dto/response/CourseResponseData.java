package com.example.sources.domain.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseResponseData {
    private Long courseId;
    private String title;
    private String description;
    private Boolean visibility;
    private String teacherName;
}
