package korobkin.nikita.bookclub.controller;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(required = false) BookGenre genre,
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sort // Список параметров сортировки
    ) {
        // Получаем книги из сервиса
        Page<Book> books = bookService.getBooks(genre, ratingMin, page, size, sort);
        return ResponseEntity.ok(books);
    }
}
