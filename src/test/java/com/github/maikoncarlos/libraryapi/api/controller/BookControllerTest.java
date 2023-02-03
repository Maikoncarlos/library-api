package com.github.maikoncarlos.libraryapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maikoncarlos.libraryapi.api.dto.BookRequestDto;
import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith (SpringExtension.class)
@ActiveProfiles ("test")
@WebMvcTest
@AutoConfigureMockMvc
class BookControllerTest {

    static String AUTHOR_UPDATE = "author update";
    static String TITLE_UPDATE = "title update";
    static String BOOK_API = "/api/books/";
    static String BOOK_API_ID = "/api/books/1";
    static Long ID = 1L;
    static String TITLE = "title";
    static String AUTHOR = "author";
    static String ISBN = "isbn";

    @Autowired
    MockMvc mockMvc;
    @MockBean
    BookService service;
    @MockBean
    ModelMapper modelMapper;

    @Test
    @DisplayName ("deve criar um livro com sucesso")
    void createBookWithSucessTest() throws Exception {

        given(service.save(any())).willReturn(createNewBook());
        String json = new ObjectMapper().writeValueAsString(requestBookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(status().isCreated());

        assertEquals(TITLE, createNewBook().getTitle());
        assertEquals(AUTHOR, createNewBook().getAuthor());
        assertEquals(ISBN, createNewBook().getIsbn());


    }

    @Test
    @DisplayName ("deve lançar erro 400 ao tentar criar um livro por ter dados inválidos na requisição ou falta de dados obrigatórios")
    void invalidToCreateBookTest() throws Exception {

        String jsonDTO = new ObjectMapper().writeValueAsString(createNewBookDTOWithInvalidsValues());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonDTO);

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));

    }

    @Test
    @DisplayName ("deve lançar erro 400 quando tentar criar um livro com o isbn já existente por outro livro")
    void createBookWithDuplicationIsbn() throws Exception {

        String messageError = "Isbn já existente";
        given(service.save(any()))
                .willThrow(new BusinessException(messageError));
        String json = new ObjectMapper().writeValueAsString(requestBookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(messageError));
    }

    @Test
    @DisplayName ("deve retornar o livro por Id com sucesso")
    void returnBookWithSucess() throws Exception {

        given(service.findById(ID)).willReturn(Optional.ofNullable(createNewBook()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API_ID)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertEquals(ID, createNewBook().getId());
        assertEquals(TITLE, createNewBook().getTitle());
        assertEquals(AUTHOR, createNewBook().getAuthor());
        assertEquals(ISBN, createNewBook().getIsbn());

    }

    @Test
    @DisplayName ("deve retornar not found quando o livro procurado por Id não existir")
    void returnBookNotFoundTest() throws Exception {

        given(service.findById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API_ID)
                .accept(MediaType.APPLICATION_JSON);

        //verificações()
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName ("deve deletar o livro do Id passado")
    void deleteBookTest() throws Exception {

        given(service.findById(anyLong())).willReturn(Optional.of(BookEntity.builder().id(ID).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API_ID);

        //verificações()
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName ("deve retornar erro404 quando tentar deletar o livro do Id passado")
    void deleteInesxistentBookTest() throws Exception {

        given(service.findById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API_ID);

        //verificações()
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName ("deve atualizar um livro com sucesso")
    void updateBookSucessTest() throws Exception {

        given(service.findById(ID)).willReturn(Optional.of(createNewBook()));
        String json = new ObjectMapper().writeValueAsString(updateBookRequestDTO());

        given(service.update(updateBookEntityRequestDTO())).willReturn(createUpdateBook());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API_ID)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertEquals(TITLE_UPDATE, createUpdateBook().getTitle());
        assertEquals(AUTHOR_UPDATE, createUpdateBook().getAuthor());
        assertEquals(ISBN, createNewBook().getIsbn());
    }

    @Test
    @DisplayName ("deve retornar erro quando tentar atualizar um livro que não existe")
    void updateErrorBookTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(requestBookDTO());

        given(service.findById(anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API_ID)
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    private BookRequestDto requestBookDTO() {
        return BookRequestDto.builder()
                .title(TITLE)
                .author(AUTHOR)
                .isbn(ISBN).build();
    }

    private BookRequestDto updateBookRequestDTO() {
        return BookRequestDto.builder()
                .title(TITLE_UPDATE)
                .author(AUTHOR_UPDATE)
                .isbn(ISBN)
                .build();
    }

    private BookEntity updateBookEntityRequestDTO() {
        return BookEntity.builder()
                .title(TITLE_UPDATE)
                .author(AUTHOR_UPDATE)
                .isbn(ISBN)
                .build();
    }

    private BookRequestDto createNewBookDTOWithInvalidsValues() {
        return BookRequestDto.builder().build();
    }

    private BookEntity createNewBook() {
        return BookEntity.builder()
                .id(ID)
                .title(TITLE)
                .author(AUTHOR)
                .isbn(ISBN).build();
    }

    private BookEntity createUpdateBook() {
        return BookEntity.builder()
                .id(ID)
                .title(TITLE_UPDATE)
                .author(AUTHOR_UPDATE)
                .isbn(ISBN)
                .build();
    }

}