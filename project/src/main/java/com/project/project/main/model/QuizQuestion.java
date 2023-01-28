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
@AllArgsConstructor
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "quiz_questions")
public class QuizQuestion {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String content;

    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photo;

    @ManyToOne
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    private List<QuizAnswer> quizAnswer;

    public static QuizQuestion fromDto(String content, MultipartFile file) throws IOException {
        return QuizQuestion.builder()
                .id(UUID.randomUUID())
                .content(content)
                .photo(file.getBytes())
                .build();
    }
}
