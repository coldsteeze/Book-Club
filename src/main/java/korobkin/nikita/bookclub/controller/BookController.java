package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.BookDTO;
import korobkin.nikita.bookclub.dto.UpdateBookDTO;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody @Valid BookDTO bookDTO) {
        bookService.createBook(bookDTO);
        return ResponseEntity.ok("Book added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable("id") int id) {
        BookDTO bookDTO = bookService.findBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable int id,
                                             @RequestBody @Valid UpdateBookDTO updatebookDTO) {
        bookService.updateBook(id, updatebookDTO);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
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
