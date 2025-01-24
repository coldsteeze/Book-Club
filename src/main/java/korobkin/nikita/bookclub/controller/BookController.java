package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody @Valid Book book) {
        bookService.createBook(book);
        return ResponseEntity.ok(book);
    }


    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(value = "genre", required = false) BookGenre genre,
            @RequestParam(value = "ratingMin", required = false) Double ratingMin,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {

        return bookService.getBooks(genre, ratingMin, page, size, sort);
    }
}
