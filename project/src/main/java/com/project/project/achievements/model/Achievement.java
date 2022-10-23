package com.project.project.achievements.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "achievements")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Achievement {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "achievement", fetch = FetchType.LAZY)
    private List<ProfileAchievement> profileAchievements;

    @Column(name = "name")
    private String name;

    public static Achievement fromDto(AchievementRequest achievementRequest){
        return Achievement.builder()
                .id(UUID.randomUUID())
                .name(achievementRequest.name())
                .build();
    }
}
