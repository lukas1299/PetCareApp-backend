package com.project.project.forum.repository;

import com.project.project.forum.model.Post;
import com.project.project.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByUser(User user);
}
