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
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "competitions_details")
public class CompetitionDetails {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private int points;

    private String petName;

    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photo;

    @ManyToOne
    @JsonIgnore
    private Competition competition;

    @OneToMany(mappedBy = "competitionDetails")
    @JsonIgnore
    private List<CompetitionResult> competitionResults;

    @OneToMany(mappedBy = "competitionDetails")
    @JsonIgnore
    private List<CompetitionDetailsAssessment> competitionDetailsAssessments;

    public static CompetitionDetails fromDto(MultipartFile file, Competition competition) throws IOException {
        return CompetitionDetails.builder()
                .id(UUID.randomUUID())
                .points(0)
                .photo(file.getBytes())
                .competition(competition)
                .build();
    }
}
