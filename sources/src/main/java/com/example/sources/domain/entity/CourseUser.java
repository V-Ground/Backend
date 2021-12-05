package com.example.sources.domain.entity;

import com.example.sources.domain.dto.aws.TaskInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskArn;
    private String containerIp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * 생성된 컨테이너의 정보를 저장한다.
     *
     * @param taskInfo : 생성된 Task 의 CLI Response DTO
     */
    public void enrollTask(TaskInfo taskInfo) {
        this.taskArn = taskInfo.getTaskArn();
        this.containerIp = taskInfo.getPublicIp();
    }
}
