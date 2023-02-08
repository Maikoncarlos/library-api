package com.github.maikoncarlos.libraryapi.api.repositories;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository repository;
    static String TITLE = "title";
    static String AUTHOR = "author";
    static String ISBN = "isbn";

    @Test
    @DisplayName("deve retornar verdadeiro quando existir um livro com o isbn informado na requisição")
    void returnTrueWhenIsbnExists() {

        //cenário
        testEntityManager.persist( bookEntity() );

        //execução
        boolean exists = repository.existsByIsbn(ISBN);

        //verificações
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("deve retornar falso quando não existir um livro com o isbn informado na requisição")
    void returnFalseWhenIsbnExist() {
        //execução
        boolean exists = repository.existsByIsbn(ISBN);

        //verificações
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("deve obter um o livro por Id com sucesso")
    void getBookByIdSucessTest(){

        BookEntity entity = bookEntity();
        testEntityManager.persist(entity);

        Optional<BookEntity> optBookEntity = repository.findById(entity.getId());

        assertThat(optBookEntity.isPresent()).isTrue();
    }

    @Test
    @DisplayName("deve salvar um livro com sucesso")
    void salvedBookWithSucessTest(){

        BookEntity entity = bookEntity();
        testEntityManager.persist(entity);

        BookEntity entitySalved = repository.save(entity);

        assertThat(entitySalved).isNotNull();
        assertThat(entitySalved.getId()).isEqualTo(entity.getId());
        assertThat(entitySalved.getTitle()).isEqualTo(entity.getTitle());
        assertThat(entitySalved.getAuthor()).isEqualTo(entity.getAuthor());
        assertThat(entitySalved.getIsbn()).isEqualTo(entity.getIsbn());
    }

    @Test
    @DisplayName("deve deletar um livro com sucesso")
    void deletedBookWithSucessTest(){

        BookEntity entity = bookEntity();
        testEntityManager.persist(entity);
        BookEntity entitySalved = testEntityManager.find(BookEntity.class, entity.getId());

        repository.delete(entitySalved);

        BookEntity bookDeleted = testEntityManager.find(BookEntity.class, entity.getId());
        assertThat(bookDeleted).isNull();
    }

    private BookEntity bookEntity() {
       return BookEntity.builder()
                .title(TITLE)
                .author(AUTHOR)
                .isbn(ISBN)
                .build();
    }

}