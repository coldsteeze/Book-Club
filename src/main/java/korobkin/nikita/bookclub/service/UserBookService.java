package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.UserBook;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import korobkin.nikita.bookclub.exception.BookDoesNotExistsException;
import korobkin.nikita.bookclub.repository.BookRepository;
import korobkin.nikita.bookclub.repository.UserBookRepository;
import korobkin.nikita.bookclub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserBookService {
    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void addBookToUser(int userId, int bookId, BookStatus bookStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookDoesNotExistsException("Book doesn't exits"));

        if (userBookRepository.findByUserIdAndBookId(userId, bookId).isPresent()) {
            throw new RuntimeException("User book already exists");
        }

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setStatus(bookStatus);

        userBookRepository.save(userBook);
    }
}
