package com.github.maikoncarlos.libraryapi.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class BookEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "valor inválido ou em branco")
    private String title;

    @NotBlank(message = "valor inválido ou em branco")
    private String author;

    @NotBlank(message = "valor inválido ou em branco")
    @Column(unique = true)
    private String isbn;
}