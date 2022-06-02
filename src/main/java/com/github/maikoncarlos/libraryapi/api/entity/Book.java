package com.github.maikoncarlos.libraryapi.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    private Long id;
    private String title;
    private String author;
    private String isbn;


}
