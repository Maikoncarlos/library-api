package com.github.maikoncarlos.libraryapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maikoncarlos.libraryapi.api.dto.BookDto;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.mapper.BookMapper;
import com.github.maikoncarlos.libraryapi.api.service.BookService;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService service;

    @MockBean
    BookMapper bookMapper;

    @Test
    @DisplayName("deve criar um livro com sucesso")
    void createBookWithSucessTest() throws Exception {

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(saveBook());
        String json = new ObjectMapper().writeValueAsString(createNewBookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(saveBook().getTitle()))
                .andExpect(jsonPath("author").value(saveBook().getAuthor()))
                .andExpect(jsonPath("isbn").value(saveBook().getIsbn()));

    }

    @Test
    @DisplayName("deve lançar erro 400 ao tentar criar um livro por ter dados inválidos na requisição ou falta de dados obrigatórios")
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
                .andExpect(jsonPath("errors" , hasSize(3)));

    }

    @Test
    @DisplayName("deve lançar erro 400 quando tentar criar um livro com o isbn já existente por outro livro")
    void createBookWithDuplicationIsbn() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewBookDTO());

        String messageError = "Isbn já existente";
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(messageError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors" , hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(messageError));
    }

    private BookDto createNewBookDTO() {
        return BookDto.builder()
                .title("title")
                .author("author")
                .isbn("isbn").build();
    }

    private BookDto createNewBookDTOWithInvalidsValues() {
        return BookDto.builder().build();
    }

    private Book saveBook(){
        return Book.builder()
                .id(1L)
                .title("title")
                .author("author")
                .isbn("isbn").build();
    }
}