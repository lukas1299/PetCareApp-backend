package com.project.project.main.service;

import com.project.project.main.model.Day;
import com.project.project.main.model.Event;
import com.project.project.main.model.EventDateRequest;
import com.project.project.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getFullEventCalendarByDay(UUID id, EventDateRequest eventDateRequest) {

        var allAnimalEvents = eventRepository.findByAnimalId(id);
        var resultList = new ArrayList<Event>();

        allAnimalEvents.forEach(event -> {
            var date = getCalendarWithoutTime(event.getDate());
            if (eventDateRequest.date().compareTo(date.getTime().toString()) == 0) {
                resultList.add(event);
            }
        });

        return resultList;
    }

    public List<Day> getFullEventCalendar(UUID id) {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        var allAnimalEvents = eventRepository.findByAnimalId(id);

        Date startDate = Date.from(LocalDate.now().minusMonths(3).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusMonths(3).atStartOfDay(defaultZoneId).toInstant());

        List<Date> datesInRange = new ArrayList<>();
        List<Day> fullEventsList = new ArrayList<>();

        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        for (Date date : datesInRange) {
            var currentEventList = new ArrayList<Event>();
            for (Event event : allAnimalEvents) {

                Calendar dateCalendar = getCalendarWithoutTime(date);
                Calendar eventCalendar = getCalendarWithoutTime(event.getDate());

                if (dateCalendar.compareTo(eventCalendar) == 0) {
                    currentEventList.add(event);
                }
            }

            fullEventsList.add(new Day(date.toString(), currentEventList));
        }

        return fullEventsList;
    }

    public List<Event> getEventByYear(UUID id, int year) {
        var eventList = eventRepository.findByAnimalId(id);

        List<Event> matchingEvents = new ArrayList<>();

        for (Event event : eventList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getDate());
            int eventYear = calendar.get(Calendar.YEAR);

            if (eventYear == year) {
                matchingEvents.add(event);
            }
        }

        return matchingEvents;
    }

    public List<Event> getEventByMonth(UUID id, int month) {
        var eventList = eventRepository.findByAnimalId(id);

        List<Event> matchingEvents = new ArrayList<>();

        for (Event event : eventList) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getDate());
            int eventMonth = calendar.get(Calendar.MONTH);

            if (eventMonth == month) {
                matchingEvents.add(event);
            }
        }

        return matchingEvents;
    }

    public List<Event> getDeprecatedEvents(UUID id) {
        var eventList = eventRepository.findByAnimalId(id);

        List<Event> matchingEvents = new ArrayList<>();

        for (Event event : eventList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(event.getDate());

            if (getCalendarWithoutTime(calendar.getTime()).before(getCalendarWithoutTime(Calendar.getInstance().getTime()))) {
                matchingEvents.add(event);
            }
        }
        matchingEvents.sort(Comparator.comparing(Event::getDate).reversed());

        return matchingEvents;
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


