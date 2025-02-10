package korobkin.nikita.bookclub.exception;

public class ReviewDoesNotExistsException extends RuntimeException {
    public ReviewDoesNotExistsException(String message) {
        super(message);
    }
}
