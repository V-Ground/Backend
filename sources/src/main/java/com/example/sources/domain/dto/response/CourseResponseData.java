package com.example.sources.domain.dto.response;

import lombok.*;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseData {
    private Long courseId;
    private String title;
    private String description;
    private Boolean visibility;
    private String containerIp;
    private String teacherName;
}
