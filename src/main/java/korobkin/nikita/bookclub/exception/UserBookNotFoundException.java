package korobkin.nikita.bookclub.exception;

public class UserBookNotFoundException extends RuntimeException {
    public UserBookNotFoundException(String message) {
        super(message);
    }
}
