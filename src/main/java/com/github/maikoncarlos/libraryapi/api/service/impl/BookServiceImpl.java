package com.github.maikoncarlos.libraryapi.api.service.impl;

import com.github.maikoncarlos.libraryapi.api.dto.BookDTO;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.mapper.BookMapper;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;
    private BookMapper bookMapper;

    @Override
    public BookDTO save(BookDTO bookDTO) {
        Book book = bookMapper.bookDTOToBook(bookDTO);
        Book save = repository.save(book);
        return bookMapper.bookToBookDTO(save);
    }
}
