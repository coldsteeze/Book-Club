package korobkin.nikita.bookclub.repository;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByGenre(BookGenre genre);
    List<Book> findByAuthor(String author);
    List<Book> findByRatingGreaterThanEqual(float rating);
    List<Book> findByIsbnIsNull();
}
