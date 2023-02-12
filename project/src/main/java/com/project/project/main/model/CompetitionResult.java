package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "competitions_result")
public class CompetitionResult {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    private Competition competition;

    @ManyToOne
    private CompetitionDetails competitionDetails;

    public static CompetitionResult fromDto(Competition competition, CompetitionDetails competitionDetails) {
        return CompetitionResult.builder()
                .id(UUID.randomUUID())
                .competition(competition)
                .competitionDetails(competitionDetails)
                .build();
    }
}
