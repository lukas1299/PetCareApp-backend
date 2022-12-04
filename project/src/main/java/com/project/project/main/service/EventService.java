package com.project.project.main.service;

import com.project.project.main.model.Day;
import com.project.project.main.model.Event;
import com.project.project.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Day> getFullEventCalendar(UUID id) {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        var allAnimalEvents = eventRepository.findByAnimalId(id);

        Date startDate = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(defaultZoneId).toInstant());
        Date endDate = Date.from(LocalDate.now().plusMonths(1).atStartOfDay(defaultZoneId).toInstant());

        List<Date> datesInRange = new ArrayList<>();
        List<Day> fullEventsList = new ArrayList<>();

        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        datesInRange.forEach(date -> {
            var currentEventList = new ArrayList<Event>();
            allAnimalEvents.forEach(event -> {

                Calendar dateCalendar = getCalendarWithoutTime(date);
                Calendar eventCalendar = getCalendarWithoutTime(event.getDate());

                if (dateCalendar.compareTo(eventCalendar) == 0) {
                    currentEventList.add(event);
                }
            });

            fullEventsList.add(new Day(date.toString(), currentEventList));
        });

        return fullEventsList;
    }

    public List<Event> getEventByYear(UUID id, int year) {

        var eventList = eventRepository.findByAnimalId(id);
        List<Event> finalEventsList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        eventList.forEach(event -> {
            try {
                var date = format.parse(event.getDate().toString());
                var eventYear = yearFormat.format(date);

                if (eventYear.equals(String.valueOf(year))) {
                    finalEventsList.add(event);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return finalEventsList;
    }

    public List<Event> getEventByMonth(UUID id, int month) {

        var eventList = eventRepository.findByAnimalId(id);
        List<Event> finalEventsList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

        eventList.forEach(event -> {
            try {
                var date = format.parse(event.getDate().toString());
                var eventYear = monthFormat.format(date);

                if (eventYear.equals(String.valueOf(month))) {
                    finalEventsList.add(event);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return finalEventsList;
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


