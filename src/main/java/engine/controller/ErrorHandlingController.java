package engine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        for (FieldError fieldError : ex.getFieldErrors()) {
            validationErrorResponse.getViolationList().add(new Violation(fieldError.getField(),fieldError.getDefaultMessage()));
        }
        return validationErrorResponse;
    }

}

class ValidationErrorResponse {

    private List<Violation> violationList = new ArrayList<>();

    public ValidationErrorResponse() {
    }

    public List<Violation> getViolationList() {
        return violationList;
    }

    public void setViolationList(List<Violation> violationList) {
        this.violationList = violationList;
    }
}

class Violation {

    private final String fieldName;
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
