package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    BookEntity save(BookEntity book);

    Optional<BookEntity> findById(Long id);

    void delete(BookEntity book);

    BookEntity update(BookEntity book);

    Page<BookEntity> find(BookEntity book, Pageable pageable);
}