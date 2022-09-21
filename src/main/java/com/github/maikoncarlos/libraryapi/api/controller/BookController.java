package com.github.maikoncarlos.libraryapi.api.controller;

import com.github.maikoncarlos.libraryapi.api.dto.BookDto;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.exceptions.ApiErrors;
import com.github.maikoncarlos.libraryapi.api.mapper.BookMapper;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto creater(@RequestBody @Valid BookDto bookDTO) {
        Book book = bookMapper.toBook(bookDTO);
        book = bookService.save(book);
        return bookMapper.toBookDTO(book);
    }

    @GetMapping("{id}")
    public BookDto getById(@PathVariable Long id){
       return bookService.getById(id)
               .map(book -> bookMapper.toBookDTO(book))
               .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessExceptions(BusinessException ex) {
        return new ApiErrors(ex);
    }
}