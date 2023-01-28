package com.project.project.main.repository;

import com.project.project.main.model.Quiz;
import com.project.project.main.model.QuizResult;
import com.project.project.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, UUID> {
    Optional<QuizResult> findByUserAndQuiz(User user, Quiz quiz);
}
