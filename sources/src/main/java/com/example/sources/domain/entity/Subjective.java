package com.example.sources.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subjective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    public void setAssignment(Assignment assignment) {
        if(this.assignment != null) {
            this.assignment.getSubjectives().remove(this);
        }
        this.assignment = assignment;
    }
}
