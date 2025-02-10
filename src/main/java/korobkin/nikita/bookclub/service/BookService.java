package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.BookDTO;
import korobkin.nikita.bookclub.dto.UpdateBookDTO;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.exception.BookAlreadyExistsException;
import korobkin.nikita.bookclub.exception.BookDoesNotExistsException;
import korobkin.nikita.bookclub.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BookService {
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("title", "author", "rating");
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public void createBook(BookDTO bookDTO) {
        if (bookRepository.existsByTitle(bookDTO.getTitle())) {
            throw new BookAlreadyExistsException("Book already exists");
        }
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setDescription(bookDTO.getDescription());
        book.setIsbn(bookDTO.getIsbn());
        bookRepository.save(book);
    }

    public void deleteBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookDoesNotExistsException("Book not found"));
        bookRepository.delete(book);
    }

    public BookDTO findBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookDoesNotExistsException("Book not found"));
        return modelMapper.map(book, BookDTO.class);
    }

    public void updateBook(int id, UpdateBookDTO updateBookDTO) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookDoesNotExistsException("Book not found"));
        if (updateBookDTO.getTitle() != null) {
            book.setTitle(updateBookDTO.getTitle());
        }
        if (updateBookDTO.getAuthor() != null) {
            book.setAuthor(updateBookDTO.getAuthor());
        }
        if (updateBookDTO.getGenre() != null) {
            book.setGenre(updateBookDTO.getGenre());
        }
        if (updateBookDTO.getDescription() != null) {
            book.setDescription(updateBookDTO.getDescription());
        }
        bookRepository.save(book);
    }

    public Page<Book> getBooks(BookGenre genre, Double ratingMin, int page, int size, String sort) {
        // Создаем объект Sort с учетом переданных параметров сортировки
        Sort sortOrder = parseAndValidateSort(sort);
        // Создаем объект Pageable для пагинации
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        // Если передан фильтр по жанру или минимальному рейтингу, фильтруем по этим параметрам
        if (genre != null || ratingMin != null) {
            return bookRepository.findBooksByFilters(genre, ratingMin, pageable);
        } else {
            return bookRepository.findAll(pageable);  // В противном случае возвращаем все книги
        }
    }

    private Sort parseAndValidateSort(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by("createdAt").descending(); // Сортировка по умолчанию
        }
        // Разделяем строку на части по запятой (field,direction)
        String[] sortParts = sort.split(",");
        if (sortParts.length < 1 || sortParts.length > 2) {
            throw new IllegalArgumentException("Invalid sort parameter: " + sort);
        }
        // Получаем поле сортировки
        String field = sortParts[0].trim();
        // Проверяем, разрешено ли сортировать по этому полю
        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException("Invalid sort field: " + field);
        }
        // Определяем направление сортировки
        Sort.Direction direction = Sort.Direction.ASC; // По умолчанию ASC
        if (sortParts.length == 2) {
            String directionString = sortParts[1].trim().toLowerCase();
            if ("desc".equals(directionString)) {
                direction = Sort.Direction.DESC;
            } else if (!"asc".equals(directionString)) {
                throw new IllegalArgumentException("Invalid sort direction: " + directionString);
            }
        }
        // Возвращаем готовую сортировку
        return Sort.by(direction, field);
    }
}
