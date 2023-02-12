package com.project.project.main.controller;

import com.project.project.main.model.AnimalBreed;
import com.project.project.main.model.AnimalBreedRequest;
import com.project.project.main.repository.AnimalBreedRepository;
import com.project.project.main.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/breeds")
public class AnimalBreedController {

    private final AnimalBreedRepository animalBreedRepository;
    private final AnimalRepository animalRepository;

    @GetMapping
    public ResponseEntity<List<AnimalBreed>> getAllBreeds() {
        return ResponseEntity.ok(animalBreedRepository.findAll());
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<AnimalBreed> getBreed(@PathVariable("id") UUID id) {
        var bread = animalBreedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Breed does not exist"));

        return ResponseEntity.ok(bread);
    }

    @GetMapping("/animal/{id}/get")
    public ResponseEntity<AnimalBreed> getBreedByAnimal(@PathVariable("id") UUID id){
        var animal = animalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Animal does not exist"));

        return ResponseEntity.ok(animal.getAnimalBreed());
    }

    @PostMapping
    public ResponseEntity<AnimalBreed> createAnimalBreed(@RequestBody AnimalBreedRequest animalBreedRequest) {
        var breed = animalBreedRepository.save(AnimalBreed.fromDto(animalBreedRequest.name()));

        return ResponseEntity.ok(breed);
    }


}
