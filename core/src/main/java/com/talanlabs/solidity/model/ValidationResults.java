package com.talanlabs.solidity.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResults {
    private List<ValidationError> errors = new ArrayList<>();

    public void addValidationError(ValidationError error) {
        this.errors.add(error);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
