package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.dto.BookDTO;

public interface BookService {
    BookDTO save(BookDTO bookDTO);
}