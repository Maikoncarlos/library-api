package com.github.maikoncarlos.libraryapi.api.service;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import com.github.maikoncarlos.libraryapi.api.repositories.BookRepository;
import com.github.maikoncarlos.libraryapi.api.service.impl.BookServiceImpl;
import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith (SpringExtension.class)
@ActiveProfiles ("test")
class BookServiceTest {

    static String ISBN = "isbn";
    static String AUTHOR = "author";
    static String TITLE = "title";
    static Long ID = 1L;
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    void setup() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName ("deve criar um livro")
    void savebookTest() {
        //cenário
        BookEntity book = bookEntity();
        when(repository.existsByIsbn(anyString())).thenReturn(false);
        when(repository.save(book)).thenReturn(newBookEntity());

        //execução
        BookEntity save = service.save(book);

        //verificação
        assertThat(save.getId()).isNotNull();
        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getAuthor()).isEqualTo("author");
        assertThat(save.getTitle()).isEqualTo("title");
        assertThat(save.getIsbn()).isEqualTo("isbn");
    }


    @Test
    @DisplayName ("deve lançar erro de negocio ao tentar salvar um livro com isbn que já exista")
    void shouldNotSaveAsBookwithDuplicateISBN() {
        //cenário
        BookEntity book = bookEntity();
        when(repository.existsByIsbn(anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificação
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já existente");

        verify(repository, never()).save(book);
    }

    @Test
    @DisplayName("deve obter livro pelo Id enviado")
    void getBookByIdSucessTest(){

        when(repository.findById(ID)).thenReturn(Optional.of(newBookEntity()));

        Optional<BookEntity> optBook = service.findById(ID);

        assertThat(optBook.isPresent()).isTrue();
        assertThat(optBook.get().getId()).isEqualTo(newBookEntity().getId());
        assertThat(optBook.get().getTitle()).isEqualTo(newBookEntity().getTitle());
        assertThat(optBook.get().getAuthor()).isEqualTo(newBookEntity().getAuthor());
        assertThat(optBook.get().getIsbn()).isEqualTo(newBookEntity().getIsbn());

    }

    @Test
    @DisplayName("deve devolver vazio por não ter livro com o Id enviado")
    void getBookByIdEmptyTest(){

        when(repository.findById(ID)).thenReturn(Optional.empty());

        Optional<BookEntity> bookEmpty = service.findById(ID);

        assertThat(bookEmpty.isPresent()).isFalse();

    }

    private static BookEntity bookEntity() {
        return BookEntity.builder()
                .title(TITLE)
                .author(AUTHOR)
                .isbn(ISBN)
                .build();
    }

    private static BookEntity newBookEntity() {
        return BookEntity.builder()
                .id(ID)
                .title(TITLE)
                .author(AUTHOR)
                .isbn(ISBN)
                .build();
    }
}