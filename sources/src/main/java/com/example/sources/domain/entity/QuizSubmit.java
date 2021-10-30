package com.example.sources.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answer;
    private Integer scored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_quiz_id")
    private EvaluationQuiz evaluationQuiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 점수를 받아 scored 의 필드에 값을 할당한다
     *
     * @param score : 점수
     */
    public void scoring(Integer score) {
        this.scored = score;
    }
}
