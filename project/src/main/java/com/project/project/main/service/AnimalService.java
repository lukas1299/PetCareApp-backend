package com.project.project.main.service;

import com.project.project.main.model.*;
import com.project.project.main.repository.AnimalRepository;
import com.project.project.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final EventRepository eventRepository;
    private final AnimalRepository animalRepository;

    public Animal createAnimal(AnimalRequest animalRequest, User user) {

        var animal = Animal.fromDto(animalRequest);
        animal.setUser(user);

        var userAnimalsList = user.getAnimals();

        if (userAnimalsList.size() == 0) {
            var newUserAnimalsList = new ArrayList<Animal>();
            newUserAnimalsList.add(animal);
            user.setAnimals(newUserAnimalsList);
        }

        animalRepository.save(animal);

        return animal;
    }

    public Event addEventToAnimal(Animal animal, EventRequest eventRequest) {
        Event event = Event.fromDto(eventRequest);

        event.setDate(String.valueOf(new Date(System.currentTimeMillis())));
        event.setAnimal(animal);

        var newEvent = eventRepository.save(event);

        var eventList = animal.getEvents();

        if (eventList.isEmpty()) {
            var newEventList = new ArrayList<Event>();
            newEventList.add(newEvent);
            animal.setEvents(newEventList);
        } else {
            eventList.add(newEvent);
            animal.setEvents(eventList);
        }

        animalRepository.save(animal);

        return newEvent;
    }
}
