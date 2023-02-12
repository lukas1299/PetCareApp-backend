package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "news_paragraph")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paragraph {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String title;
    private String text;
    private int counter;

    @ManyToOne
    @JsonIgnore
    private News news;

    public static Paragraph fromDto(ParagraphRequest paragraphRequest, int number) {
        return Paragraph.builder()
                .id(UUID.randomUUID())
                .title(paragraphRequest.title())
                .text(paragraphRequest.text())
                .counter(number)
                .build();
    }

}
