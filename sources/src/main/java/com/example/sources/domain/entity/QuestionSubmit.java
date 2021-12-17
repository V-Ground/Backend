package com.example.sources.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answer;
    private Integer scored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private CourseQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 문제와 user 를 세팅하여 문제를 해결한다.
     *
     * @param question : 문제
     * @param user : 사용자
     */
    public void solve(CourseQuestion question, User user) {
        this.question = question;
        this.user = user;
    }

    /**
     * 정답을 갱신한다.
     *
     * @param answer : 갱신할 정답
     */
    public void updateAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 점수를 입력한다.
     *
     * @param score : 입력할 점수
     */
    public void scoring(Integer score) {
        this.scored = score;
    }

    /**
     * 자동 채점을 진행한다.
     *
     * @param inputAnswer 학생이 입력한 답
     * @param answer 강사가 설정한 답
     */
    public void score(String inputAnswer, String answer) {
        this.scored = inputAnswer.equals(answer) ? 1 : 0;
    }
}
