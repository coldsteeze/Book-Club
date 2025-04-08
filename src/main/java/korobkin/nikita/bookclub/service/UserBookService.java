package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.BookDto;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.UserBook;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import korobkin.nikita.bookclub.exception.BookDoesNotExistsException;
import korobkin.nikita.bookclub.exception.UserBookNotFoundException;
import korobkin.nikita.bookclub.repository.BookRepository;
import korobkin.nikita.bookclub.repository.UserBookRepository;
import korobkin.nikita.bookclub.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserBookService {
    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public void addBookToUser(int userId, int bookId, BookStatus bookStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BookDoesNotExistsException("Book doesn't exist")
        );

        log.info("User '{}' is adding book '{}' to their collection with status '{}'",
                user.getUsername(), book.getTitle(), bookStatus);

        if (userBookRepository.findByUserIdAndBookId(userId, bookId).isPresent()) {
            throw new RuntimeException("User book already exists");
        }

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setStatus(bookStatus);

        userBookRepository.save(userBook);
    }

    public List<BookDto> getBooksByFilter(int userId, BookStatus bookStatus) {
        log.info("User '{}' is fetching books with status '{}' from their collection",
                userId, bookStatus != null ? bookStatus : "ALL");

        List<Book> books = (bookStatus != null) ?
                userBookRepository.findBooksByBookStatusAndUserId(userId, bookStatus) :
                userBookRepository.findBooksByUserId(userId);

        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public void updateUserBookStatus(int userId, int bookId, BookStatus bookStatus) {
        log.info("User '{}' is updating book status for book ID '{}' to '{}'",
                userId, bookId, bookStatus);

        UserBook userBook = userBookRepository.findBookByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new UserBookNotFoundException("User book not found"));
        userBook.setStatus(bookStatus);
        userBookRepository.save(userBook);
    }

    public void deleteUserBook(int userId, int bookId) {
        log.info("User '{}' is deleting book with ID '{}' from their collection",
                userId, bookId);

        UserBook userBook = userBookRepository.findBookByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new UserBookNotFoundException("User book not found"));
        userBookRepository.delete(userBook);
    }

    public List<BookGenre> getTopThreeGenres(int userId) {
        return userBookRepository.findTopGenresByUserId(userId, PageRequest.of(0, 3)).getContent();
    }
}

