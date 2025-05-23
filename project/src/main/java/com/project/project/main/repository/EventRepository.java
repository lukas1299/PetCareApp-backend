package com.project.project.main.repository;

import com.project.project.main.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByAnimalId(UUID id);
    List<Event> findByNameAndAnimalId(String name, UUID animalId);
}
