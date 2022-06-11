package com.github.maikoncarlos.libraryapi.api.controller;

import com.github.maikoncarlos.libraryapi.api.dto.BookDTO;
import com.github.maikoncarlos.libraryapi.api.exceptions.ApiErrors;
import com.github.maikoncarlos.libraryapi.api.mapper.BookMapper;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private BookMapper bookMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO creater(@RequestBody @Valid BookDTO bookDTO) {
        return bookService.save(bookDTO);
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