package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.dto.BookDto;
import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.impl.BookServiceImpl;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
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
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("deve criar um livro")
    void savebookTest() {
        //cenário
        BookEntity book = createNewBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn
                (BookEntity.builder().id(1L).author("author").title("title").isbn("isbn").build());

        //execução
        BookEntity save = service.save(book);

        //verificação
        assertThat(save.getId()).isNotNull();
        assertThat(save.getAuthor()).isEqualTo("author");
        assertThat(save.getTitle()).isEqualTo("title");
        assertThat(save.getIsbn()).isEqualTo("isbn");
    }

    @Test
    @DisplayName("deve lançar erro de negocio ao tentar salvar um livro com isbn que já exista")
    void shouldNotSaveAsBookwithDuplicateISBN() {
        //cenário
        BookEntity book = createNewBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));
        
        //verificação
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já existente");

        Mockito.verify(repository, Mockito.never()).save(book);
    }


    private BookDto createNewBookDTO() {
        return BookDto.builder().title("title").author("author").isbn("isbn").build();
    }

    private BookEntity createNewBook() {
        return BookEntity.builder().title("title").author("author").isbn("isbn").build();
    }
}