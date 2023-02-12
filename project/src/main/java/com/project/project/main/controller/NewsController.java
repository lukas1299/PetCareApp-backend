package com.project.project.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.model.*;
import com.project.project.main.repository.NewsRepository;
import com.project.project.main.repository.ParagraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/news")
public class NewsController {

    private final NewsRepository newsRepository;
    private final ParagraphRepository paragraphRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<NewsResponse>> getAllNews() {
        var news = newsRepository.findAll();

        return ResponseEntity.ok(news.stream().map(n -> new NewsResponse(n, n.getParagraphList().stream().sorted(Comparator.comparing(Paragraph::getCounter)).toList())).toList());
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable("id") UUID id) {
        var news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("News does not exist"));

        var paragraphs = news.getParagraphList().stream().sorted(Comparator.comparing(Paragraph::getCounter)).toList();

        var response = new NewsResponse(news, paragraphs);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestParam("file") MultipartFile file, @RequestParam("json") String json) throws IOException {

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        var requestNews = objectMapper.readValue(json, NewsRequest.class);

        var news = News.fromDto(requestNews.title(), file.getBytes(), localDateTime.format(format));

        newsRepository.save(news);

        return ResponseEntity.ok(news);
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<Paragraph> createParagraph(@PathVariable("id") UUID id, @RequestBody ParagraphRequest paragraphRequest) {

        var news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("News does not exist"));

        int counter = 0;

        if (news.getParagraphList() != null) {
            counter = news.getParagraphList().size();
        }

        var paragraph = Paragraph.fromDto(paragraphRequest, counter + 1);

        paragraph.setNews(news);

        paragraphRepository.save(paragraph);

        return ResponseEntity.ok().build();
    }

}
