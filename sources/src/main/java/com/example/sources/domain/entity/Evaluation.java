package com.example.sources.domain.entity;

import com.example.sources.domain.type.OsType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String cpu;
    private String memory;
    private String os;
    private Boolean visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    /**
     * evaluation 를 생성한다.
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
}
