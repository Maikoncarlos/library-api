package com.github.maikoncarlos.libraryapi.api.exceptions;

import com.github.maikoncarlos.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {
    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(objectError -> this.errors.add(objectError.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors(){
        return errors;
    }
}
