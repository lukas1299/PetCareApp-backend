package com.project.project.main.repository;

import com.project.project.main.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {

    @Query(value = "SELECT * FROM friends WHERE profile_id = ?1", nativeQuery = true)
    List<Friend> findByProfile_id(UUID id);

    @Query(value = "SELECT * FROM friends WHERE profile_id= ?1 AND user_id = ?2" ,nativeQuery = true)
    Friend findByProfileIdAndUserId(UUID profileId, UUID userId);

}
