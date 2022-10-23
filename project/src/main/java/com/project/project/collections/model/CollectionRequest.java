package com.project.project.collections.model;

import java.math.BigDecimal;
import java.util.Date;

public record CollectionRequest (String title, BigDecimal target, String description, Date creationDate) {
}
