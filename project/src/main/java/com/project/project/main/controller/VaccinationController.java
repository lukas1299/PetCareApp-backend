package com.project.project.main.controller;

import com.project.project.main.model.Vaccination;
import com.project.project.main.model.VaccinationRequest;
import com.project.project.main.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/vaccinations")
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationRepository vaccinationRepository;

    @GetMapping
    public ResponseEntity<List<Vaccination>> getAllVaccinations(){
        return ResponseEntity.ok(vaccinationRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Vaccination> createVaccination(@RequestBody VaccinationRequest vaccinationRequest) {
        var vaccination = vaccinationRepository.save(Vaccination.fromDto(vaccinationRequest.name(), vaccinationRequest.interval()));
        return ResponseEntity.ok(vaccination);
    }
}
