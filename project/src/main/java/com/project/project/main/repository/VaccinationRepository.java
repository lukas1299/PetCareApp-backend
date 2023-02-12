package com.project.project.main.repository;

import com.project.project.main.model.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, UUID> {
    List<Vaccination> findByName(String name);
}
