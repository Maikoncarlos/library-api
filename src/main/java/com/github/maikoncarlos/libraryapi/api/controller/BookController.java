package com.github.maikoncarlos.libraryapi.api.controller;

import com.github.maikoncarlos.libraryapi.api.dto.BookRequestDto;
import com.github.maikoncarlos.libraryapi.api.dto.BookResponseDto;
import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.exceptions.ApiErrors;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api/books/")
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public BookResponseDto creater(@RequestBody @Valid BookRequestDto bookRequestDTO) {
        BookEntity entity = modelMapper.map(bookRequestDTO, BookEntity.class);
        return modelMapper.map(bookService.save(entity), BookResponseDto.class);
    }

    @GetMapping ("{id}")
    public BookResponseDto findById(@PathVariable Long id) {
        BookEntity entity = bookService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(entity, BookResponseDto.class);
    }

    @DeleteMapping ("{id}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        BookEntity bookEntity = bookService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        bookService.delete(bookEntity);
    }

    @PutMapping ("{id}")
    public BookResponseDto update(@PathVariable Long id, @RequestBody BookRequestDto bookRequestDTO) {
        bookService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        BookEntity bookEntity = modelMapper.map(bookRequestDTO, BookEntity.class);
        return modelMapper.map(bookEntity, BookResponseDto.class);
    }

    @GetMapping()
    public Page<BookResponseDto> find(BookRequestDto requestDto, Pageable pageable){
        BookEntity book = modelMapper.map(requestDto, BookEntity.class);
        Page<BookEntity> resultPage = bookService.find( book,pageable );
        List<BookResponseDto> list = resultPage.getContent()
                .stream()
                .map( pageResult -> modelMapper.map(pageResult, BookResponseDto.class))
                .collect(Collectors.toList());

        return new PageImpl<BookResponseDto>(list, pageable, resultPage.getTotalElements());
    }


    @ExceptionHandler (MethodArgumentNotValidException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler (BusinessException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessExceptions(BusinessException ex) {
        return new ApiErrors(ex);
    }
}