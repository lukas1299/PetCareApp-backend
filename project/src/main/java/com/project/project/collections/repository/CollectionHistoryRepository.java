package com.project.project.collections.repository;

import com.project.project.collections.model.CollectionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionHistoryRepository extends JpaRepository<CollectionHistory, UUID> {
    Optional<List<CollectionHistory>> findByCollection_id(UUID id);
}
