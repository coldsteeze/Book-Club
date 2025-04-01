package korobkin.nikita.bookclub.repository;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE "
            + "(:genre IS NULL OR b.genre = :genre) AND "
            + "(:ratingMin IS NULL OR b.rating >= :ratingMin)")
    Page<Book> findBooksByFilters(
            @Param("genre") BookGenre genre,
            @Param("ratingMin") Double ratingMin,
            Pageable pageable
    );

    @Query("SELECT b FROM Book b WHERE b.genre IN :genres AND b.id NOT IN "
            + "(SELECT ub.book.id FROM UserBook ub WHERE ub.user.id = :userId)")
    List<Book> findRecommendedBooks(@Param("userId") int userId, @Param("genres") List<BookGenre> genres);

    Boolean existsByTitle(String title);
}
