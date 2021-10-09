package com.example.sources.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Assignment assignment;

    @OneToMany(mappedBy = "assignmentSubmit")
    private List<SubjectiveAnswer> subjectiveAnswer;
}
