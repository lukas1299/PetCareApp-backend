package com.project.project.main.repository;

import com.project.project.main.model.Profile;
import com.project.project.main.model.SocialPost;
import com.project.project.main.model.SocialPostsAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SocialPostsAssessmentRepository extends JpaRepository<SocialPostsAssessment, UUID> {
    List<SocialPostsAssessment> findByProfileAndSocialPost(Profile profile, SocialPost socialPost);
}
