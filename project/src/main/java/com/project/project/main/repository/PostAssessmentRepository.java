package com.project.project.main.repository;

import com.project.project.forum.model.Post;
import com.project.project.main.model.PostAssessment;
import com.project.project.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostAssessmentRepository extends JpaRepository<PostAssessment, UUID> {
    List<PostAssessment> findByPostAndUser(Post post, User user);

    List<PostAssessment> findByPost(Post post);
}
