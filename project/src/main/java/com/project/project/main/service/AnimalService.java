package com.project.project.main.service;

import com.project.project.main.model.*;
import com.project.project.main.repository.AnimalRepository;
import com.project.project.main.repository.EventRepository;
import com.project.project.main.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimalService {

    private final EventRepository eventRepository;
    private final AnimalRepository animalRepository;
    private final VaccinationRepository vaccinationRepository;

    public Animal createAnimal(AnimalRequest animalRequest, User user, MultipartFile file, AnimalBreed animalBreed) throws IOException {

        var animal = Animal.fromDto(animalRequest, file.getBytes(), animalBreed);
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

    public VaccinationResponse checkVaccinationTime(Animal animal) throws ParseException {
        List<Event> finalList = new ArrayList<>();
        var allVaccinations = vaccinationRepository.findAll();

        for (Vaccination vaccination : allVaccinations) {
            var latestVaccination = getTheLatestVaccination(animal.getId(), vaccination.getName());
            if (!latestVaccination.isEmpty()) {
                finalList.add(latestVaccination.get(latestVaccination.size() - 1));
            }
        }

        Calendar now = Calendar.getInstance();
        var list =  finalList.stream()
                .filter(event -> {
                    Calendar nextVaccination = Calendar.getInstance();
                    nextVaccination.setTime(event.getDate());
                    VaccinationInterval interval = vaccinationRepository.findByName(event.getName().substring(0, event.getName().length() - 4)).get(0).getInterval();

                    if (interval == VaccinationInterval.EVERY_HALF_YEAR) {
                        nextVaccination.add(Calendar.MONTH, 6);
                    } else if (interval == VaccinationInterval.EVERY_YEAR) {
                        nextVaccination.add(Calendar.YEAR, 1);
                    } else {
                        nextVaccination.add(Calendar.YEAR, 2);
                    }
                    return nextVaccination.get(Calendar.MONTH) == now.get(Calendar.MONTH) && nextVaccination.get(Calendar.YEAR) == now.get(Calendar.YEAR);
                })
                .map(event -> {
                    VaccinationInterval interval = vaccinationRepository.findByName(event.getName().substring(0, event.getName().length() - 4)).get(0).getInterval();
                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();
                    calendar.setTime(event.getDate());
                    calendar1.setTime(event.getDate());
                    if (interval == VaccinationInterval.EVERY_HALF_YEAR) {
                        calendar.add(Calendar.MONTH, 6);
                        calendar1.add(Calendar.MONTH, 6);
                    } else if (interval == VaccinationInterval.EVERY_YEAR) {
                        calendar.add(Calendar.YEAR, 1);
                        calendar1.add(Calendar.YEAR, 1);
                    } else {
                        calendar.add(Calendar.YEAR, 2);
                        calendar1.add(Calendar.YEAR, 2);
                    }

                    calendar.add(Calendar.MONTH, 1);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String maxData = dateFormat.format(calendar.getTime());
                    String minData = dateFormat.format(calendar1.getTime());

                    return new VaccinationResponse(event.getName().substring(0, event.getName().length() - 4), minData, maxData);
                })
                .findFirst()
                .orElse(new VaccinationResponse("", "", ""));

        if(!Objects.equals(list.name(), "")){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = dateFormat.parse(list.minDate());
            Calendar vacDate = Calendar.getInstance();
            vacDate.setTime(date);
            if (vacDate.before(Calendar.getInstance())){
                Calendar currentDate = Calendar.getInstance();
                Calendar currentDatePlusMonth = Calendar.getInstance();
                currentDatePlusMonth.add(Calendar.MONTH, 1);

                return new VaccinationResponse(list.name(),dateFormat.format(currentDate.getTime()), dateFormat.format(currentDatePlusMonth.getTime()));
            }
            log.warn(vacDate.getTime().toString());
        }
        return list;
    }

    public List<Event> addEventToAnimal(Animal animal, EventRequest eventRequest) throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        calendar.setTimeZone(TimeZone.getTimeZone(defaultZoneId));
        calendar.setTime(sdf.parse(eventRequest.date()));

        if (getCalendarWithoutTime(calendar.getTime()).before(getCalendarWithoutTime(Calendar.getInstance().getTime()))) {
            throw new Exception("The event cannot be created because the date is invalid");
        }

        return createEvent(animal, eventRequest, calendar.getTime());
    }

    public List<Event> removeEvents(EventDeleteRequest eventDeleteRequest) throws Exception {

        var eventList = eventRepository.findByNameAndAnimalId(eventDeleteRequest.name(), eventDeleteRequest.animalId());
        if (eventList.get(0).getEventType() == EventType.VACCINATION) {
            var vaccination = eventList.get(0);

            Calendar currentCalendar = Calendar.getInstance();

            Calendar vaccinationCalendar = Calendar.getInstance();
            vaccinationCalendar.setTime(vaccination.getDate());

            if (vaccinationCalendar.before(currentCalendar)) {
                throw new Exception("Vaccinations that have already taken place cannot be removed");
            }
            eventRepository.delete(vaccination);

        } else {
            eventRepository.deleteAll(eventList);
        }

        return eventList;
    }

    private List<Event> createEvent(Animal animal, EventRequest eventRequest, Date date) throws Exception {

        List<Event> result = new ArrayList<>();
        Event event;

        if (eventRequest.eventType() == EventType.VACCINATION) {

            if (vaccinationRepository.findByName(eventRequest.name()).isEmpty()) {
                throw new Exception("Vaccination does not exist");
            }

            var finishedVaccination = getTheLatestVaccination(animal.getId(), eventRequest.name());
            if (finishedVaccination.isEmpty()) {
                event = new Event(UUID.randomUUID(), prepareEventName(animal, eventRequest.name()), date, eventRequest.eventType(), animal);
                result.add(saveEvent(event, animal, date));
            } else {
                var lastEvent = finishedVaccination.get(finishedVaccination.size() - 1);
                var vaccination = vaccinationRepository.findByName(lastEvent.getName().substring(0, lastEvent.getName().length() - 4)).get(0);

                if (checkExpiration(vaccination, lastEvent, date)) {
                    throw new Exception("Vaccination cannot take place during this time");
                } else {
                    event = new Event(UUID.randomUUID(), prepareEventName(animal, eventRequest.name()), date, eventRequest.eventType(), animal);
                    result.add(saveEvent(event, animal, date));
                }
            }
        } else {

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

            event = Event.fromDto(eventRequest);

            switch (eventRequest.frequency()) {
                case "once":
                    tempEventList.add(saveEvent(event, animal, calendar.getTime()));
                    break;
                case "everyday":
                    for (int i = 0; i <= 90; i++) {
                        var eventDate = Date.from(localDate.plusDays(i).atStartOfDay(defaultZoneId).toInstant());
                        tempEventList.add(saveEvent(event, animal, eventDate));
                    }
                    break;
                case "everyweek":
                    for (int i = 0; i <= 12; i++) {
                        var eventDate = Date.from(localDate.plusWeeks(i).atStartOfDay(defaultZoneId).toInstant());
                        tempEventList.add(saveEvent(event, animal, eventDate));
                    }
                    break;
                case "everymonth":
                    for (int i = 0; i <= 3; i++) {
                        var eventDate = Date.from(localDate.plusMonths(i).atStartOfDay(defaultZoneId).toInstant());
                        tempEventList.add(saveEvent(event, animal, eventDate));
                    }
                    break;
            }
            result = tempEventList;
        }

        return result;
    }

    private Event saveEvent(Event e, Animal animal, Date date) {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setAnimal(e.getAnimal());
        event.setName(e.getName());
        event.setEventType(e.getEventType());
        event.setDate(date);
        event.setAnimal(animal);

        var newEvent = eventRepository.save(event);

        var eventList = animal.getEvents();
        eventList.add(newEvent);
        animal.setEvents(eventList);

        animalRepository.save(animal);

        return event;
    }

    private boolean checkExpiration(Vaccination v, Event e, Date date) {
        Calendar event = Calendar.getInstance();
        event.setTime(e.getDate());
        Calendar check = Calendar.getInstance();
        check.setTime(date);
        if (v.getInterval() == VaccinationInterval.EVERY_HALF_YEAR) {
            event.add(Calendar.MONTH, 6);
        } else if (v.getInterval() == VaccinationInterval.EVERY_YEAR) {
            event.add(Calendar.YEAR, 1);
        } else {
            event.add(Calendar.YEAR, 2);
        }
        return check.before(event);
    }

    public String prepareEventName(Animal animal, String name) {
        List<Event> vaccinations = getTheLatestVaccination(animal.getId(), name);

        if (vaccinations.isEmpty()) {
            return name + " (1)";
        }

        Event latestEvent = vaccinations.get(vaccinations.size() - 1);
        String eventName = latestEvent.getName();
        int count = Integer.parseInt(eventName.substring(eventName.length() - 2, eventName.length() - 1));
        return eventName.substring(0, eventName.length() - 3) + "(" + (count + 1) + ")";
    }

    private List<Event> getTheLatestVaccination(UUID id, String name) {
        return eventRepository.findByAnimalId(id)
                .stream()
                .filter(event -> event.getEventType() == EventType.VACCINATION)
                .filter(event -> event.getName().contains(name))
                .sorted(Comparator.comparingInt(o -> o.getName().charAt(o.getName().length() - 2)))
                .toList();
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
