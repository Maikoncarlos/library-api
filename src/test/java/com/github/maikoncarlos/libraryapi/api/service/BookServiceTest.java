package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.dto.BookDto;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    void setup() {
        this.service = new BookServiceImpl();
    }

    @Test
    @DisplayName("Deve Criar um Livro")
    void savebookTest() {
        //cenário
        Book book = Book.builder().id(10L).author("author").title("title").isbn("isbn").build();
        Mockito.when(repository.save(book)).thenReturn
                (Book.builder().author("author").title("title").isbn("isbn").build());

        //execução
        BookDto save = service.save(createNewBookDTO());

        //verificação
       // assertThat(save.getId()).isNotNull();
        assertThat(save.getAuthor()).isEqualTo("author");
        assertThat(save.getTitle()).isEqualTo("title");
        assertThat(save.getIsbn()).isEqualTo("isbn");
    }

    private BookDto createNewBookDTO() {
        return com.github.maikoncarlos.libraryapi.api.dto.BookDto.builder().title("title").author("author").isbn("isbn").build();
    }
}
