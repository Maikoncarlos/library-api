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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

        Optional<BookEntity> bookEmpty = service.findById(anyLong());

        assertThat(bookEmpty.isPresent()).isFalse();

    }

    @Test
    @DisplayName("deve deletar um livro com sucesso ")
    void deleteBookSucessTest(){

        BookEntity entity = newBookEntity();

        assertDoesNotThrow(() -> service.delete(entity));

        verify(repository, times(1)).delete(entity);

    }

    @Test
    @DisplayName("deve devolver erro quando tentar deletar livro inexistente ")
    void deleteErrorBookTest(){

        BookEntity entity = BookEntity.builder().build();

        assertThrows(IllegalArgumentException.class,() -> service.delete(entity));

        verify(repository, never()).delete(entity);

    }

    @Test
    @DisplayName("deve update um livro com sucesso ")
    void updateBookSucessTest(){

        BookEntity updateEntity = updateBookEntity();

        when(repository.save(updateEntity)).thenReturn(updateEntity);

        BookEntity book = assertDoesNotThrow(() -> service.update(updateEntity));

        verify(repository, times(1)).save(updateEntity);

        assertThat(book.getId()).isEqualTo(updateEntity.getId());
        assertThat(book.getTitle()).isEqualTo(updateEntity.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updateEntity.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updateEntity.getIsbn());

    }

    @Test
    @DisplayName("deve devolver erro quando tentar update livro inexistente ")
    void updateErrorBookTest(){

        BookEntity entity = BookEntity.builder().build();

        assertThrows(IllegalArgumentException.class,() -> service.update(entity));

        verify(repository, never()).save(entity);

    }

    @Test
    @DisplayName("deve devolver uma Page de Livro")
    void findBookPageTeste(){

        BookEntity entity = bookEntity();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<BookEntity> entityList = Arrays.asList(entity);
        Page<BookEntity> page = new PageImpl<BookEntity>(entityList,pageRequest, 1);
        when(repository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);

        Page<BookEntity> pages = service.find(entity, pageRequest);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).isEqualTo(entityList);
        assertThat(page.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(page.getPageable().getPageSize()).isEqualTo(10);


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

    private static BookEntity updateBookEntity() {
        return BookEntity.builder()
                .id(ID)
                .title("titulo update")
                .author("author update")
                .isbn(ISBN)
                .build();
    }
}