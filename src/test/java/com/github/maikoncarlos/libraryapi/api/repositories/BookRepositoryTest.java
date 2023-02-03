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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository repository;

    static String ISBN = "isbn";

    @Test
    @DisplayName("deve retornar verdadeiro quando existir um livro com o isbn informado na requisição")
    void returnTrueWhenIsbnExists() {

        //cenário
        BookEntity book = BookEntity.builder().title("title").author("author").isbn(ISBN).build();
        testEntityManager.persist(book);

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

}