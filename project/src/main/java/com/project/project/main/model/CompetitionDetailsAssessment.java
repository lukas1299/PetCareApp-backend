package com.project.project.main.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "competitions_details_assessment")
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Builder
public class CompetitionDetailsAssessment {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    private CompetitionDetails competitionDetails;

    @ManyToOne
    private Profile profile;

    public static CompetitionDetailsAssessment fromDto(CompetitionDetails competitionDetails, Profile profile){
        return CompetitionDetailsAssessment.builder()
                .id(UUID.randomUUID())
                .competitionDetails(competitionDetails)
                .profile(profile)
                .build();
    }
}
