package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.BookDto;
import korobkin.nikita.bookclub.dto.BookResponse;
import korobkin.nikita.bookclub.dto.UpdateBookDto;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody @Valid BookDto bookDto) {
        log.info("Adding new book: {}", bookDto.getTitle());
        bookService.createBook(bookDto);
        return ResponseEntity.ok("Book added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") int bookId) {
        log.info("Getting book by id: {}", bookId);
        BookDto bookDto = bookService.findBookById(bookId);
        return ResponseEntity.ok(bookDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable("id") int bookId,
                                             @RequestBody @Valid UpdateBookDto updateBookDto) {
        log.info("Updating book with id: {}", bookId);
        bookService.updateBook(bookId, updateBookDto);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable("id") int bookId) {
        log.info("Deleting book with id: {}", bookId);
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @GetMapping
    public Page<BookResponse> getBooks(
            @RequestParam(value = "genre", required = false) BookGenre genre,
            @RequestParam(value = "ratingMin", required = false) Double ratingMin,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {
        log.info("Getting books with page: {}, size: {}, sort: {}", page, size, sort);
        return bookService.getBooks(genre, ratingMin, page, size, sort);
    }

    @GetMapping("/recommendations")
    public List<BookResponse> getRecommendations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Getting recommendations for user: {}", userDetails.getUsername());
        return bookService.getRecommendedBooks(userDetails.getUser());
    }
}
