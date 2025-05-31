package ru.terentyev.technomant_testtasak.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.terentyev.technomant_testtasak.models.Article;
import ru.terentyev.technomant_testtasak.models.ArticleCreateRequest;
import ru.terentyev.technomant_testtasak.models.ArticleResponse;
import ru.terentyev.technomant_testtasak.repositories.ArticleRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService {


    private final ArticleRepository articleRepository;


    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleResponse createArticle(ArticleCreateRequest articleCreateRequest) {
        Article article = new Article();
        BeanUtils.copyProperties(articleCreateRequest, article);
        try {
            article = articleRepository.save(article);
        } catch (DataAccessException e) {
            System.err.println("Exception during article creation: " + e.getMessage());
            throw e;
        }
        ArticleResponse articleResponse = new ArticleResponse();
        BeanUtils.copyProperties(article, articleResponse);
        return articleResponse;
    }


    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }
    public List<Article> findByPublishingDateBetween(LocalDate startDate, LocalDate endDate) {
        return articleRepository.findByPublishingDateBetween(startDate, endDate);
    }
}
