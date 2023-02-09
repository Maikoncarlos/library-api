package com.github.maikoncarlos.libraryapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryApiApplicationTest {

        @Test
        @DisplayName ("Classe Principal")
        void main() {
            LibraryApiApplication.main(new String[]{});
        }

    }
