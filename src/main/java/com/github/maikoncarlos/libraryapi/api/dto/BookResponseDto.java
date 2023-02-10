package com.github.maikoncarlos.libraryapi.api.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class BookResponseDto {
    private String title;
    private String author;
    private String isbn;
}
