package ru.terentyev.technomant_testtasak.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.terentyev.technomant_testtasak.models.Article;
import ru.terentyev.technomant_testtasak.models.ArticleCreateRequest;
import ru.terentyev.technomant_testtasak.models.ArticleResponse;
import ru.terentyev.technomant_testtasak.services.ArticleService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;


    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@Valid @RequestBody ArticleCreateRequest articleCreateRequest) {
        ArticleResponse createdArticle = articleService.createArticle(articleCreateRequest);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Article> pageTuts = articleService.findAll(paging);
        List<ArticleResponse> articles = pageTuts.getContent().stream()
                .map(article -> {
                    ArticleResponse articleResponse = new ArticleResponse();
                    BeanUtils.copyProperties(article, articleResponse);
                    return articleResponse;
                })
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articles);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<LocalDate, Long>> getArticleStatistics() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        List<Article> articles = articleService.findByPublishingDateBetween(startDate, endDate);
        Map<LocalDate, Long> dailyCounts = articles.stream()
                .collect(Collectors.groupingBy(Article::getPublishingDate, Collectors.counting()));
        return new ResponseEntity<>(dailyCounts, HttpStatus.OK);
    }
}