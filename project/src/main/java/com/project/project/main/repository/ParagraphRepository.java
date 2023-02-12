package com.project.project.main.repository;

import com.project.project.main.model.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, UUID> {
}
