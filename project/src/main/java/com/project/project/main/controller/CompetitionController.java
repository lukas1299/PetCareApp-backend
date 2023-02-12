package com.project.project.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.exception.ObjectNotFoundException;
import com.project.project.main.model.*;
import com.project.project.main.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/competitions")
public class CompetitionController {

    private final CompetitionRepository competitionRepository;
    private final CompetitionDetailsRepository competitionDetailsRepository;
    private final CompetitionResultRepository competitionResultRepository;
    private final CompetitionDetailsAssessmentRepository competitionDetailsAssessmentRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Competition>> getAllCompetitions() {
        var competitions = competitionRepository.findAll();
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<CompetitionDetails>> getCompetitionsDetails(@PathVariable("id") UUID id) {
        var competition = competitionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Competition does not exist"));
        var details = competition.getDetails()
                .stream()
                .sorted(Comparator.comparing(CompetitionDetails::getPoints).reversed())
                .toList();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/finished")
    public ResponseEntity<List<CompetitionResult>> getCompetitionsResults(){
        return ResponseEntity.ok(competitionResultRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Competition> createCompetition(@RequestParam("file") MultipartFile file, @RequestParam("json") String json) throws IOException {

        var competition = Competition.fromDto(objectMapper.readValue(json, CompetitionRequest.class).title(), file);
        competitionRepository.save(competition);
        return ResponseEntity.ok(competition);
    }

    @PostMapping("/{id}/finish")
    public void finishCompetition(@PathVariable("id") UUID id) throws Exception {
        var competition = competitionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Competition does not exist"));
        var competitionDetail = competitionDetailsRepository.findByCompetition(competition).stream().max(Comparator.comparing(CompetitionDetails::getPoints)).get();
        var result = competitionResultRepository.findByCompetition(competition);
        if(result.isPresent()){
            throw new Exception("The competition is now over");
        }
        competitionResultRepository.save(CompetitionResult.fromDto(competition, competitionDetail));
    }

    @PostMapping("/{id}/details/add")
    public ResponseEntity<CompetitionDetails> createCompetitionDetails(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile file) throws IOException {

        var competition = competitionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Competition does not exist"));
        var competitionDetail = CompetitionDetails.fromDto(file, competition);

        competitionDetailsRepository.save(competitionDetail);
        return ResponseEntity.ok(competitionDetail);
    }

    @PostMapping("/details/{id}/rate")
    public ResponseEntity<?> implementCompetitionRate(Authentication authentication, @PathVariable("id") UUID id) {
        var competitionDetails = competitionDetailsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Competition details does not exist"));
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        List<CompetitionDetailsAssessment> assessments = competitionDetailsAssessmentRepository.findByProfile(profile);

        boolean conflictFound = false;
        for (CompetitionDetailsAssessment assessment : assessments) {
            if (assessment.getCompetitionDetails().getCompetition().getId() == competitionDetails.getCompetition().getId()) {
                conflictFound = true;
                break;
            }
        }
        if (conflictFound) {
            return new ResponseEntity<>("The competition is already rated", HttpStatus.CONFLICT);
        }

        competitionDetails.setPoints(competitionDetails.getPoints() + 1);

        var rate = CompetitionDetailsAssessment.fromDto(competitionDetails, profile);

        competitionDetailsAssessmentRepository.save(rate);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}
