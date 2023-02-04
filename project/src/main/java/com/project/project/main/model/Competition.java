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
@RequiredArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
@Table(name = "competitions")
public class Competition {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String title;

    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photo;

    @OneToMany(mappedBy = "competition")
    @JsonIgnore
    private List<CompetitionDetails> details;

    @OneToMany(mappedBy = "competition")
    @JsonIgnore
    private List<CompetitionResult> competitionResults;

    public static Competition fromDto(String title, MultipartFile multipartFile) throws IOException {
        return Competition.builder()
                .id(UUID.randomUUID())
                .title(title)
                .photo(multipartFile.getBytes())
                .build();
    }
}
