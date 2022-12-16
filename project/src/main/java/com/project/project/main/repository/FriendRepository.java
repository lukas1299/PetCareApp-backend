package com.project.project.main.repository;

import com.project.project.main.model.Friend;
import com.project.project.main.model.Profile;
import com.project.project.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID> {

    Optional<Friend> findByProfileAndUser(Profile myProfile, User user);
    List<Friend> findByUserId(UUID id);
    List<Friend> findByProfile(Profile profile);
}
