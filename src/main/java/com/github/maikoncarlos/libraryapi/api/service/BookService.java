package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;

import java.util.Optional;

public interface BookService {

    BookEntity save(BookEntity book);

    Optional<BookEntity> getById(Long id);
}