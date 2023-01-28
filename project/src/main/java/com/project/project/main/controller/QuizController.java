package com.project.project.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.model.Quiz;
import com.project.project.main.model.QuizRequest;
import com.project.project.main.model.QuizResponse;
import com.project.project.main.model.QuizResult;
import com.project.project.main.repository.QuizRepository;
import com.project.project.main.repository.QuizResultRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizRepository quizRepository;
    private final QuizService quizService;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/get")
    public ResponseEntity<List<QuizResponse>> getTenRandomQuizzes(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        List<QuizResponse> resultList = new ArrayList<>();

        var quizzes = quizRepository.findAll();

        for(Quiz quiz : quizzes){
            var quizResult = quizResultRepository.findByUserAndQuiz(user, quiz);
            if(quizResult.isPresent()){
                resultList.add(new QuizResponse(quiz, quizResult.get().getResult()));
            } else {
                resultList.add(new QuizResponse(quiz, "0"));
            }
        }

        return ResponseEntity.ok(resultList);
    }

    @PostMapping("/add")
    public ResponseEntity<Quiz> addQuiz(@RequestParam("file") MultipartFile file, @RequestParam("json") String json) throws IOException {
        var requestQuiz = objectMapper.readValue(json, QuizRequest.class);
        var quiz = Quiz.fromDto(requestQuiz.topic(), file);
        quizRepository.save(quiz);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{id}/{result}/saveResult")
    private ResponseEntity<String> saveQuizResult(@PathVariable("id") UUID id, @PathVariable("result") int result, Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var quiz = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The quiz does not exist"));

        var percent = String.valueOf(result * 10);

        quizResultRepository.findByUserAndQuiz(user, quiz)
                .ifPresentOrElse(
                        quizResult -> {
                            if (Integer.parseInt(quizResult.getResult()) < result * 10) {
                                quizResult.setResult(percent);
                                quizResultRepository.save(quizResult);
                            }
                        },
                        () -> quizResultRepository.save(QuizResult.fromDto(user, quiz, percent))
                );

        return ResponseEntity.ok(percent);
    }
}
