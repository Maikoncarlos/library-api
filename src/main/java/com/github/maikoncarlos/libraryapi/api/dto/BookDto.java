package com.github.maikoncarlos.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long id;
    @NotBlank(message = "valor inválido ou em branco")
    private String title;
    @NotBlank(message = "valor inválido ou em branco")
    private String author;
    @NotBlank(message = "valor inválido ou em branco")
    private String isbn;
}
