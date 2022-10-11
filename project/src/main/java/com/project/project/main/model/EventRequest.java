package com.project.project.main.model;

import java.util.Date;

public record EventRequest(String name, Date data, EventType eventType) {
}
