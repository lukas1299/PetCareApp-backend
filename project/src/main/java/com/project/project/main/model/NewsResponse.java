package com.project.project.main.model;

import java.util.List;

public record NewsResponse(News news, List<Paragraph> paragraphList) {
}
