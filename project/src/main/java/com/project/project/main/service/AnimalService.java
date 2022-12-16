package com.project.project.main.service;

import com.project.project.main.model.*;
import com.project.project.main.repository.AnimalRepository;
import com.project.project.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

    public List<Event> addEventToAnimal(Animal animal, EventRequest eventRequest) throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        List<Event> tempEventList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        calendar.setTimeZone(TimeZone.getTimeZone(defaultZoneId));
        calendar.setTime(sdf.parse(eventRequest.date()));

        if (getCalendarWithoutTime(calendar.getTime()).before(getCalendarWithoutTime(Calendar.getInstance().getTime()))) {
            throw new Exception("The event cannot be created because the date is invalid");
        }

        LocalDate localDate = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();

        final int numDays = 90;
        final int numWeeks = 12;
        final int numMonths = 3;

        switch (eventRequest.frequency()) {
            case "once":
                tempEventList.add(createEvent(animal, eventRequest, calendar.getTime()));
                break;
            case "everyday":
                for (int i = 0; i <= numDays; i++) {
                    var eventDate = Date.from(localDate.plusDays(i).atStartOfDay(defaultZoneId).toInstant());
                    tempEventList.add(createEvent(animal, eventRequest, eventDate));
                }
                break;
            case "everyweek":
                for (int i = 0; i <= numWeeks; i++) {
                    var eventDate = Date.from(localDate.plusWeeks(i).atStartOfDay(defaultZoneId).toInstant());
                    tempEventList.add(createEvent(animal, eventRequest, eventDate));
                }
                break;
            case "everymonth":
                for (int i = 0; i <= numMonths; i++) {
                    var eventDate = Date.from(localDate.plusMonths(i).atStartOfDay(defaultZoneId).toInstant());
                    tempEventList.add(createEvent(animal, eventRequest, eventDate));
                }
                break;
        }

        return tempEventList;
    }

    public List<Event> removeEvents(EventDeleteRequest eventDeleteRequest) {

        var eventList = eventRepository.findByNameAndAnimalId(eventDeleteRequest.name(), eventDeleteRequest.animalId());

        eventRepository.deleteAll(eventList);

        return eventList;
    }

    private Event createEvent(Animal animal, EventRequest eventRequest, Date date) {
        var event = Event.fromDto(eventRequest);
        event.setDate(date);
        event.setAnimal(animal);

        var newEvent = eventRepository.save(event);

        var eventList = animal.getEvents();
        eventList.add(newEvent);
        animal.setEvents(eventList);

        animalRepository.save(animal);
        return event;
    }

    private static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
