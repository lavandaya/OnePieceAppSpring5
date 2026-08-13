package be.kdg.programming5.onepiece.presentation.controller.api;

import be.kdg.programming5.onepiece.business.exception.CharacterNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import be.kdg.programming5.onepiece.business.exception.CrewNotFoundException;
import be.kdg.programming5.onepiece.business.exception.NotASwordsmanException;
import be.kdg.programming5.onepiece.presentation.dto.ApiErrorDto;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = CharacterRestController.class)
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(CharacterNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(CharacterNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        logger.debug("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDto> handleUnreadableBody(HttpMessageNotReadableException ex) {
        logger.debug("Malformed request body: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiErrorDto("Request body is malformed or contains an invalid value"));
    }

    @ExceptionHandler(CrewNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleCrewNotFound(CrewNotFoundException ex) {
        logger.debug("Unknown crew referenced: {}", ex.getCrewName());
        return ResponseEntity.badRequest().body(new ApiErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(NotASwordsmanException.class)
    public ResponseEntity<ApiErrorDto> handleNotASwordsman(NotASwordsmanException ex) {
        logger.debug("Sword update rejected for character id={}", ex.getCharacterId());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorDto(ex.getMessage()));
    }
}