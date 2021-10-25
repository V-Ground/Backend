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

}
