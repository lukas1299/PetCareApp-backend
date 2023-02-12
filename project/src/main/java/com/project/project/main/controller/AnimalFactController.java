package com.project.project.main.controller;

import com.project.project.main.model.AnimalFact;
import com.project.project.main.model.AnimalFactRequest;
import com.project.project.main.repository.AnimalBreedRepository;
import com.project.project.main.repository.AnimalFactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/facts")
public class AnimalFactController {

    private final AnimalFactRepository animalFactRepository;
    private final AnimalBreedRepository animalBreedRepository;

    @GetMapping
    public ResponseEntity<List<AnimalFact>> getAllFacts(){
        return ResponseEntity.ok(animalFactRepository.findAll());
    }

    @GetMapping("{name}/getByBreed")
    public ResponseEntity<List<AnimalFact>> getFactsByBreed(@PathVariable("name") String name) throws Exception {
        var breed = animalBreedRepository.findByName(name).orElseThrow(() -> new Exception("Breed does not exist"));

        List<AnimalFact> list;

        if(Objects.equals(breed.getName(), "no breed") || Objects.equals(breed.getName(), "mixed breed")){
            list = animalFactRepository.findAll();
            var indexes = getTwoRandomIndex(list.size());
            return ResponseEntity.ok(List.of(list.get(indexes.get(0)), list.get(indexes.get(1))));
        }

        list = breed.getAnimalFactList();
        var indexes = getTwoRandomIndex(list.size());

        return ResponseEntity.ok(List.of(list.get(indexes.get(0)), list.get(indexes.get(1))));
    }

    @PostMapping("/{id}/breed/add")
    public ResponseEntity<AnimalFact> addAnimalFact(@PathVariable("id") UUID id, @RequestBody AnimalFactRequest animalFactRequest) {

        var breed = animalBreedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Breed does not exist"));
        var fact = AnimalFact.fromDto(animalFactRequest.name());
        fact.setAnimalBreed(breed);
        animalFactRepository.save(fact);

        return ResponseEntity.ok(fact);
    }

    private List<Integer> getTwoRandomIndex(int range){
        Random random = new Random();

        while (true){
            int firstRandomIndex = random.nextInt(range);
            int secondRandomIndex = random.nextInt(range);
            if(firstRandomIndex != secondRandomIndex){
                return List.of(firstRandomIndex, secondRandomIndex);
            }
        }
    }
}
