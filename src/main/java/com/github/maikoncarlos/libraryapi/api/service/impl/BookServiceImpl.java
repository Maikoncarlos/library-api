package com.github.maikoncarlos.libraryapi.api.service.impl;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public BookEntity save(BookEntity book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn já existente");
        }
        return repository.save(book);
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(BookEntity book) {
        repository.delete(book);
    }

    @Override
    public BookEntity update(BookEntity book) {
        return repository.save(book);
    }


}
