package com.github.maikoncarlos.libraryapi.api.controller;

import com.github.maikoncarlos.libraryapi.api.dto.BookRequestDto;
import com.github.maikoncarlos.libraryapi.api.dto.BookResponseDto;
import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


import java.util.Arrays;
import java.util.Collections;

import static com.github.maikoncarlos.libraryapi.api.controller.BookControllerTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookControllerFindTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    private BookEntity bookEntity;
    private BookResponseDto responseDto;
    private BookRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        startBooks();
    }

//    @Test
//    @DisplayName("deeve retornar Book Paginado")
//    void findBookPageTest() {
//
//        PageRequest of = PageRequest.of(0, 100);
//        Pageable any = any(Pageable.class);
//        when(bookService.find( any(),any )).
//                thenReturn(new PageImpl<BookEntity>(Arrays.asList(bookEntity), of, 1));
//
//        when(mapper.map(any(), any())).thenReturn(bookEntity);
//
//
//        Page<BookResponseDto> responseDtoPage = bookController.find( requestDto, any);
//
//        assertNotNull(responseDtoPage);
//
//    }

    @Test
    @DisplayName("deve criar um Book com Sucesso")
    void createrBookWithSucessTest() {

        when(modelMapper.map(bookService.save(bookEntity), BookResponseDto.class)).thenReturn(responseDto);

        BookResponseDto response = bookController.creater(requestDto);

        assertNotNull(response);
        assertEquals(TITLE, response.getTitle());
        assertEquals(AUTHOR, response.getAuthor());
        assertEquals(ISBN, response.getIsbn());
    }

    private void startBooks() {
        bookEntity = new BookEntity(ID, TITLE, AUTHOR, ISBN);
        responseDto = new BookResponseDto(TITLE, AUTHOR, ISBN);
        requestDto = new BookRequestDto(ID, TITLE, AUTHOR, ISBN);
    }
}