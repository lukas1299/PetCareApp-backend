package com.project.project.collections.repository;

import com.project.project.collections.model.Collection;
import com.project.project.main.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    List<Collection> findByProfile(Profile profile);
}
