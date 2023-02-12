package com.project.project.main.repository;

import com.project.project.main.model.Competition;
import com.project.project.main.model.CompetitionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompetitionResultRepository extends JpaRepository<CompetitionResult, UUID> {
    Optional<CompetitionResult> findByCompetition(Competition competition);
}
