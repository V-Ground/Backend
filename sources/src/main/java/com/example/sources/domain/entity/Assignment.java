package com.example.sources.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder.Default
    @OneToMany(mappedBy = "assignment")
    private List<Subjective> subjectives = new ArrayList<>();

    public void addSubjectives(Subjective subjective) {
        subjectives.add(subjective);
        subjective.setAssignment(this);
    }
}
