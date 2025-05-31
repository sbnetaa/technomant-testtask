package ru.terentyev.technomant_testtasak.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleCreateRequest extends AbstractDTO {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    @NotBlank(message = "Author is mandatory")
    private String author;
    @NotBlank(message = "Content is mandatory")
    private String content;
    @NotNull(message = "Publishing date is mandatory")
    private LocalDate publishingDate;
}
