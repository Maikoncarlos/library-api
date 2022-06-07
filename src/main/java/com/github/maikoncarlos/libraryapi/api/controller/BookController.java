package com.github.maikoncarlos.libraryapi.api.controller;

import com.github.maikoncarlos.libraryapi.api.dto.BookDTO;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.mapper.BookMapper;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private BookMapper bookMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO creater(@RequestBody BookDTO bookDTO) {

         Book entity = bookMapper.bookDTOToBook(bookDTO);


//        Book entity =
//                Book.builder()
//                        .title(bookDTO.getTitle())
//                        .author(bookDTO.getAuthor())
//                        .isbn(bookDTO.getIsbn())
//                        .build();

        entity = bookService.save(entity);

        BookDTO dto = bookMapper.bookToBookDTO(entity);

//        final BookDTO dto = BookDTO.builder()
//                .id(entity.getId())
//                .title(entity.getTitle())
//                .author(entity.getAuthor())
//                .isbn(entity.getIsbn())
//                .build();

        return dto;

    }
}
