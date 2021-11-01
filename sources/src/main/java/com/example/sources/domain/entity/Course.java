package com.example.sources.domain.entity;

import com.example.sources.domain.type.OsType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String cpu;
    private String memory;
    private OsType os;
    private Boolean visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    /**
     * course 를 생성한다.
     *
     * @param user : teacher
     * @return
     */
    public void create(User user) {
        this.teacher = user;
        this.visibility = true;
    }

    /**
     * course 의 visibility 를 invisible 로 변경한다
     */
    public void disable() {
        this.visibility = false;
    }

    /**
     * 파라미터로 들어오는 user 가 동일한 user 인지 확인한다.
     *
     * @param userId : 비교할 User 의 id
     * @return 동일한 사용자라면 true
     */
    public boolean isOwner(Long userId) {
        return userId.equals(this.id);
    }
}
