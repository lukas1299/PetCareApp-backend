package com.project.project.main.repository;

import com.project.project.main.model.Profile;
import com.project.project.main.model.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocialPostRepository extends JpaRepository<SocialPost, UUID> {
    Optional<List<SocialPost>> findByProfile(Profile profile);
}
