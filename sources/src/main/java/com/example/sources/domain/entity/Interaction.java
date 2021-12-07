package com.example.sources.domain.entity;

import com.example.sources.domain.type.InteractionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public void publish(Course course) {
        this.course = course;
        this.createdAt = LocalDateTime.now();
    }
}
