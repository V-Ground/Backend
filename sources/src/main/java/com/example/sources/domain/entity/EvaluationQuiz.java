package com.example.sources.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String description;
    private String answer;
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Evaluation evaluation;

    public void create(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
