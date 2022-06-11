package com.github.maikoncarlos.libraryapi.api.mapper;

import com.github.maikoncarlos.libraryapi.api.dto.BookDto;
import com.github.maikoncarlos.libraryapi.api.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toBookDTO(Book book);

    Book toBook(BookDto bookDTO);
}
