package com.project.project.main.model;

public record EventRequest(String name, EventType eventType, String frequency, String date) {
}
