package com.project.project.main.repository;

import com.project.project.main.model.Animal;
import com.project.project.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Optional<Animal> findByName(String name);
    List<Animal> findByUser(User user);
}

