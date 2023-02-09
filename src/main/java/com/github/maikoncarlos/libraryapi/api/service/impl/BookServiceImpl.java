package com.github.maikoncarlos.libraryapi.api.service.impl;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if( book == null || book.getId() == null ){
           throw new IllegalArgumentException(" Book id cant be null! ");
        }
        repository.delete(book);
    }

    @Override
    public BookEntity update(BookEntity book) {
        if( book == null || book.getId() == null ){
            throw new IllegalArgumentException(" Book id cant be null! ");
        }
        return repository.save(book);
    }

    @Override
    public Page<BookEntity> find(BookEntity book, Pageable pageable) {
        Example<BookEntity> exampe = Example.of(book,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(exampe, pageable);
    }


}
