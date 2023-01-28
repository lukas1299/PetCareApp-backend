package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "quiz_results")
public class QuizResult {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String result;

    @ManyToOne
    private User user;

    @ManyToOne
    private Quiz quiz;

    public static QuizResult fromDto(User user, Quiz quiz, String result) {
        return QuizResult.builder()
                .id(UUID.randomUUID())
                .user(user)
                .quiz(quiz)
                .result(result)
                .build();
    }
}
