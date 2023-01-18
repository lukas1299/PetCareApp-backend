package com.project.project.collections.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record CollectionRequest (String title, BigDecimal target, String description, Date creationDate) {
}
