package com.github.maikoncarlos.libraryapi.api.service.impl;

import com.github.maikoncarlos.libraryapi.api.dto.BookDto;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.mapper.BookMapper;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;
    private BookMapper bookMapper;


    public BookDto save(@Valid BookDto bookDTO) {
        Book book = bookMapper.toBook(bookDTO);
        Book save = repository.save(book);
        return bookMapper.toBookDTO(save);
    }
}
