package korobkin.nikita.bookclub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message(errors.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiError> handleAuthenticationFailedException(AuthenticationFailedException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(BookDoesNotExistsException.class)
    public ResponseEntity<ApiError> handleBookDoesNotExists(BookDoesNotExistsException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ReviewDoesNotExistsException.class)
    public ResponseEntity<ApiError> handleReviewDoesNotExists(ReviewDoesNotExistsException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserBookNotFoundException.class)
    public ResponseEntity<ApiError> handleBookUserNotFound(UserBookNotFoundException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleReviewAlreadyExists(ReviewAlreadyExistsException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleBookAlreadyExists(BookAlreadyExistsException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ApiError> handlePassword(PasswordException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Conflict")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
