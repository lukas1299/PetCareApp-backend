package com.project.project.forum.repository;

import com.project.project.forum.model.Topic;
import com.project.project.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    List<Topic> findByUser(User user);
    List<Topic> findByTitleContainingIgnoreCase(String title);
}
