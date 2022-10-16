package com.project.project.main.service;

import com.project.project.main.model.Event;
import com.project.project.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getEventByYear(Long id, int year) {

        var eventList = eventRepository.findByAnimalId(id);
        List<Event> finalEventsList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat yearFormat= new SimpleDateFormat("yyyy");

        eventList.forEach(event -> {
            try {
                var date = format.parse(event.getDate().toString());
                var eventYear = yearFormat.format(date);

                if(eventYear.equals(String.valueOf(year))){
                    finalEventsList.add(event);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return finalEventsList;
    }

    public List<Event> getEventByMonth(Long id, int month){

        var eventList = eventRepository.findByAnimalId(id);
        List<Event> finalEventsList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat monthFormat= new SimpleDateFormat("MM");

        eventList.forEach(event -> {
            try {
                var date = format.parse(event.getDate().toString());
                var eventYear = monthFormat.format(date);

                if(eventYear.equals(String.valueOf(month))){
                    finalEventsList.add(event);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return finalEventsList;
    }
}


