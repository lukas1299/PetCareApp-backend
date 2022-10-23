package com.project.project.achievements.controller;

import com.project.project.achievements.model.Achievement;
import com.project.project.achievements.model.AchievementRequest;
import com.project.project.achievements.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementRepository achievementRepository;

    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievement() {
        return ResponseEntity.ok(achievementRepository.findAll());
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<List<Achievement>> getAllAchievement(@PathVariable UUID id) {
        return ResponseEntity.ok(achievementRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Achievement> addAchievement(@RequestBody AchievementRequest achievementRequest){

        return ResponseEntity.ok(achievementRepository.save(Achievement.fromDto(achievementRequest)));
    }
}
