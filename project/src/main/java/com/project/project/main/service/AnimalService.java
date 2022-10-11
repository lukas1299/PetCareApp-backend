package com.project.project.main.service;

import com.project.project.main.model.*;
import com.project.project.main.repository.AnimalRepository;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;

    public Animal createAnimal(AnimalRequest animalRequest, User user){

        var animal = new Animal();
        animal.setName(animalRequest.name());
        animal.setType(animalRequest.animalType());
        animal.setAge(animalRequest.age());
        animal.setWeight(animalRequest.weight());
        animal.setAnimalGender(animalRequest.gender());
        animal.setUser(user);

        var userAnimalsList = user.getAnimals();

        if(userAnimalsList.size() == 0){
            var newUserAnimalsList = new ArrayList<Animal>();
            newUserAnimalsList.add(animal);
            user.setAnimals(newUserAnimalsList);
        }

        userRepository.save(user);

        return animal;
    }

    public Event addEventToAnimal(Animal animal, EventRequest eventRequest) {
        Event event = Event.fromDto(eventRequest);

        event.setAnimal(animal);
        var eventList = animal.getEvents();

        if(eventList.isEmpty()){

            var newEventList = new ArrayList<Event>();
            newEventList.add(event);
            animal.setEvents(newEventList);
        }else {
            eventList.add(event);
            animal.setEvents(eventList);
        }

        animalRepository.save(animal);

        return eventRepository.save(event);
    }
}
