package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.BookDto;
import korobkin.nikita.bookclub.dto.BookResponse;
import korobkin.nikita.bookclub.dto.ReviewResponse;
import korobkin.nikita.bookclub.dto.UpdateBookDto;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.exception.BookAlreadyExistsException;
import korobkin.nikita.bookclub.exception.BookDoesNotExistsException;
import korobkin.nikita.bookclub.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class BookService {
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("title", "author", "rating");
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final UserBookService userBookService;

    public void createBook(BookDto bookDto) {
        log.info("Creating new book with title: {}", bookDto.getTitle());
        if (bookRepository.existsByTitle(bookDto.getTitle())) {
            log.info("Cannot create book: '{}' already exists", bookDto.getTitle());
            throw new BookAlreadyExistsException("Book already exists");
        }
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setGenre(bookDto.getGenre());
        book.setDescription(bookDto.getDescription());
        book.setIsbn(bookDto.getIsbn());
        bookRepository.save(book);
        log.info("Book '{}' created successfully", bookDto.getTitle());
    }

    public void deleteBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookDoesNotExistsException("Book not found")
        );
        bookRepository.delete(book);
        log.info("Book with id {} deleted successfully", book.getTitle());
    }

    public BookDto findBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookDoesNotExistsException("Book not found")
        );
        log.info("Book with id {} found", id);
        return modelMapper.map(book, BookDto.class);
    }

    public void updateBook(int id, UpdateBookDto updateBookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new BookDoesNotExistsException("Book not found")
        );
        if (updateBookDto.getTitle() != null) {
            book.setTitle(updateBookDto.getTitle());
        }
        if (updateBookDto.getAuthor() != null) {
            book.setAuthor(updateBookDto.getAuthor());
        }
        if (updateBookDto.getGenre() != null) {
            book.setGenre(updateBookDto.getGenre());
        }
        if (updateBookDto.getDescription() != null) {
            book.setDescription(updateBookDto.getDescription());
        }
        bookRepository.save(book);
        log.info("Book with id {} updated successfully", id);
    }

    public Page<BookResponse> getBooks(BookGenre genre, Double ratingMin, int page, int size, String sort) {
        Sort sortOrder = parseAndValidateSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Book> books = (genre != null || ratingMin != null) ?
                bookRepository.findBooksByFilters(genre, ratingMin, pageable) :
                bookRepository.findAll(pageable);

        List<BookResponse> bookResponses = books.getContent().stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
        log.info("Successfully retrieved books with page: {}, size: {}, sort: {}", page, size, sort);
        return new PageImpl<>(bookResponses, pageable, books.getTotalElements());
    }

    public List<BookResponse> getRecommendedBooks(User user) {
        List<BookGenre> genres = userBookService.getTopThreeGenres(user.getId());
        List<Book> books = bookRepository.findRecommendedBooks(user.getId(), genres);
        log.info("Successfully retrieved recommended books for user: {}", user.getUsername());
        return books.stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }

    private BookResponse convertToBookResponse(Book book) {
        return new BookResponse(
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getDescription(),
                book.getIsbn(),
                book.getReviews() != null ? book.getReviews().stream()
                        .map(review -> new ReviewResponse(review.getText(), review.getRating()))
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    private Sort parseAndValidateSort(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by("createdAt").descending();
        }
        String[] sortParts = sort.split(",");
        if (sortParts.length < 1 || sortParts.length > 2) {
            throw new IllegalArgumentException("Invalid sort parameter: " + sort);
        }
        String field = sortParts[0].trim();

        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException("Invalid sort field: " + field);
        }

        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParts.length == 2) {
            String directionString = sortParts[1].trim().toLowerCase();
            if ("desc".equals(directionString)) {
                direction = Sort.Direction.DESC;
            } else if (!"asc".equals(directionString)) {
                throw new IllegalArgumentException("Invalid sort direction: " + directionString);
            }
        }

        return Sort.by(direction, field);
    }
}










