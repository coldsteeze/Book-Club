package korobkin.nikita.bookclub.exception;

public class BookDoesNotExistsException extends RuntimeException {
    public BookDoesNotExistsException(String message) {
        super(message);
    }
}
