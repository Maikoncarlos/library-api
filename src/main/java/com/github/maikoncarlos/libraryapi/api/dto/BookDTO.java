package com.github.maikoncarlos.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder()
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
}
