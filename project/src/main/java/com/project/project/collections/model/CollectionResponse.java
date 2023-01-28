package com.project.project.collections.model;

import com.project.project.main.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CollectionResponse(UUID id, User user, String title, BigDecimal target, String description, String creationDate,
                                 float percentages, List<CollectionHistory> donates) {
}
