package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.dto.BookDto;

import javax.validation.Valid;

public interface BookService {

    BookDto save(@Valid BookDto bookDTO);
}