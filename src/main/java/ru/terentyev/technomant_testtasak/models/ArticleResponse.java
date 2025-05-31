package ru.terentyev.technomant_testtasak.models;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleResponse extends AbstractDTO {

    private String title;
    private String author;
    private String content;
    private LocalDate publishingDate;
}
