package com.project.project.main.repository;

import com.project.project.main.model.CompetitionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompetitionDetailsRepository extends JpaRepository<CompetitionDetails, UUID> {
}
