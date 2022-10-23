package com.project.project.achievements.model;

import com.project.project.main.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "profile_achievement")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileAchievement {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public static ProfileAchievement formDto(Achievement achievement, Profile profile){
        return ProfileAchievement.builder()
                .id(UUID.randomUUID())
                .achievement(achievement)
                .profile(profile)
                .build();
    }

}
