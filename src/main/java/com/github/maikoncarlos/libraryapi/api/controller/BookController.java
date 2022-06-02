package com.github.maikoncarlos.libraryapi.api.controller;

import com.github.maikoncarlos.libraryapi.api.dto.BookDTO;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO creater(@RequestBody BookDTO bookDTO) {
        Book bookEntity =
                Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .isbn(bookDTO.getIsbn())
                .build();

        bookEntity = bookService.save(bookEntity);

        BookDTO dto =
                BookDTO.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .isbn(bookEntity.getIsbn())
                .build();
        return dto;
    }
}
