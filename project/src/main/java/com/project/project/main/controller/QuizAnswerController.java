package com.project.project.main.controller;

import com.project.project.main.model.AnswerRequest;
import com.project.project.main.model.QuizAnswer;
import com.project.project.main.repository.QuizAnswerRepository;
import com.project.project.main.repository.QuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answers")
public class QuizAnswerController {
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<QuizAnswer> getById(@PathVariable("id") UUID id) {
        var answer = quizAnswerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The answer does not exist"));
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/question/{id}/add")
    public ResponseEntity<QuizAnswer> addAnswerToQuestion(@PathVariable("id") UUID id, @RequestBody AnswerRequest answerRequest){
        var question = quizQuestionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The question does not exist"));

        var answer = QuizAnswer.fromDto(answerRequest.answer(), answerRequest.correctness());
        answer.setQuestion(question);
        quizAnswerRepository.save(answer);
        var answers = question.getQuizAnswer();
        answers.add(answer);

        question.setQuizAnswer(answers);
        quizQuestionRepository.save(question);
        return ResponseEntity.ok(answer);
    }

}
