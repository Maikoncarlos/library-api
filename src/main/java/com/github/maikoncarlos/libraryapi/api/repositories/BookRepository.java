package com.github.maikoncarlos.libraryapi.api.repositories;

import com.github.maikoncarlos.libraryapi.api.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsByIsbn(String isbn);
}