package com.project.project.main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import javax.persistence.*;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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
                .competition(competition)
                .competitionDetails(competitionDetails)
                .build();
    }
}
