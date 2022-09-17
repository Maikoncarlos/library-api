package com.github.maikoncarlos.libraryapi.api.repositories;

import com.github.maikoncarlos.libraryapi.api.entity.Book;
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

    @Test
    @DisplayName("deve retornar verdadeiro quando existir um livro com o isbn informado na requisição")
    void returnTrueWhenIsbnExists() {
        //cenário
        String isbn = "isbn";
        Book book = Book.builder().title("title").author("author").isbn(isbn).build();
        testEntityManager.persist(book);

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificações
        assertThat(exists).isTrue();
    }

}