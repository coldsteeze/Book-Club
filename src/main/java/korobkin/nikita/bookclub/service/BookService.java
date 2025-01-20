package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("title", "author", "rating");

    private final BookRepository bookRepository;

    public void createBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    public Book findBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void updateBook(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    public Page<Book> getBooks(BookGenre genre, Double ratingMin, int page, int size, List<String> sort) {
        // Создаем объект Sort
        Sort sortOrder = parseAndValidateSort(sort);

        // Создаем объект Pageable
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        // Используем метод репозитория с фильтрацией и пагинацией
        if (genre != null || ratingMin != null) {
            return bookRepository.findBooksByFilters(genre, ratingMin, pageable);
        } else {
            return bookRepository.findAll(pageable);
        }
    }

    private Sort parseAndValidateSort(List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by("createdAt").descending(); // Сортировка по умолчанию
        }

        // Парсинг параметров сортировки
        Sort sortOrder = Sort.unsorted();
        for (String sortParam : sort) {
            String[] sortParts = sortParam.split(",");
            String field = sortParts[0].trim();
            Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1])
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            // Проверяем, разрешено ли сортировать по этому полю
            if (!ALLOWED_SORT_FIELDS.contains(field)) {
                throw new IllegalArgumentException("Invalid sort field: " + field);
            }

            sortOrder = sortOrder.and(Sort.by(direction, field));
        }
        return sortOrder;
    }
}
