package com.project.project.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.model.QuizQuestion;
import com.project.project.main.model.QuizQuestionRequest;
import com.project.project.main.repository.QuizQuestionRepository;
import com.project.project.main.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/questions")
public class QuizQuestionController {

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}/get")
    public ResponseEntity<List<QuizQuestion>> getQuestion(@PathVariable("id") UUID id){
        var questions = quizQuestionRepository.findByQuizId(id);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/quiz/{id}/add")
    public ResponseEntity<QuizQuestion> addQuestionToQuiz(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile file, @RequestParam("json") String json) throws IOException {
        var quiz = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The quiz does not exist"));
        var request = objectMapper.readValue(json, QuizQuestionRequest.class);
        var question = QuizQuestion.fromDto(request.content(), file);

        question.setQuiz(quiz);
        quizQuestionRepository.save(question);
        var questions = quiz.getQuestions();
        questions.add(question);

        quiz.setQuestions(questions);
        quizRepository.save(quiz);
        return ResponseEntity.ok(question);
    }
}
