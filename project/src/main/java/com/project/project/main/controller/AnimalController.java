package com.project.project.main.controller;

import com.project.project.main.model.Animal;
import com.project.project.main.model.RequestAnimal;
import com.project.project.main.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals(){
        return ResponseEntity.ok(animalRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Animal> createAnimal(@RequestBody RequestAnimal requestAnimal){

        var animal = buildAnimal(requestAnimal);
        return new ResponseEntity<>(animalRepository.save(animal), HttpStatus.CREATED);
    }

    private Animal buildAnimal(RequestAnimal requestAnimal){
        return Animal.builder()
                .id(0L)
                .name(requestAnimal.name())
                .type(requestAnimal.animalType())
                .age(requestAnimal.age()).build();
    }
}
