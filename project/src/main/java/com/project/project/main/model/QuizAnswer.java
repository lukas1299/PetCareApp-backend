package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "quiz_answers")
public class QuizAnswer {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String answer;
    private boolean correctness;

    @ManyToOne
    @JsonIgnore
    private QuizQuestion question;

    public static QuizAnswer fromDto(String answer, boolean correctness) {
        return QuizAnswer.builder()
                .id(UUID.randomUUID())
                .answer(answer)
                .correctness(correctness)
                .build();
    }
}
