package ru.terentyev.technomant_testtasak.models;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Article extends AbstractEntity {

    private String title;
    private String author;
    private String content;
    private LocalDate publishingDate;

    public Article(){}
}
