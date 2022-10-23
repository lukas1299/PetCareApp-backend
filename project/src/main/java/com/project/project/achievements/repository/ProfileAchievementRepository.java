package com.project.project.achievements.repository;

import com.project.project.achievements.model.ProfileAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileAchievementRepository extends JpaRepository<ProfileAchievement, UUID> {
}
