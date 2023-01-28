package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "quizzes")
public class Quiz {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String topic;

    @Type(type="org.hibernate.type.BinaryType")
    private byte[] photo;

    @OneToMany(mappedBy = "quiz")
    @JsonIgnore
    private List<QuizQuestion> questions;

    @OneToMany(mappedBy = "quiz")
    @JsonIgnore
    private List<QuizResult> results;

    public static Quiz fromDto(String topic, MultipartFile file) throws IOException {
        return Quiz.builder()
                .id(UUID.randomUUID())
                .topic(topic)
                .photo(file.getBytes())
                .build();
    }
}
