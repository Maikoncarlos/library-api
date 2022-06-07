package com.github.maikoncarlos.libraryapi.api.dto;

import lombok.*;

@Data
@Builder()
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
}
