package com.project.project.main.repository;

import com.project.project.main.model.CompetitionDetailsAssessment;
import com.project.project.main.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompetitionDetailsAssessmentRepository extends JpaRepository<CompetitionDetailsAssessment, UUID> {
    List<CompetitionDetailsAssessment> findByProfile(Profile profile);
}
