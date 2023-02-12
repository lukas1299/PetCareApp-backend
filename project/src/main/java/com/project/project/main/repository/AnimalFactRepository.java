package com.project.project.main.repository;

import com.project.project.main.model.AnimalFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnimalFactRepository extends JpaRepository<AnimalFact, UUID> {
}
