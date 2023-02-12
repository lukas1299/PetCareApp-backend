package com.project.project.main.repository;

import com.project.project.main.model.AnimalBreed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimalBreedRepository extends JpaRepository<AnimalBreed, UUID> {
    Optional<AnimalBreed> findByName(String breed);
}
