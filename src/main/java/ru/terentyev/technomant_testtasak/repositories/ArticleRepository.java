package ru.terentyev.technomant_testtasak.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.terentyev.technomant_testtasak.models.Article;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Page<Article> findAll(Pageable pageable);
    List<Article> findByPublishingDateBetween(LocalDate startDate, LocalDate endDate);
}
