package korobkin.nikita.bookclub.repository;


import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.UserBook;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Integer> {
    Optional<UserBook> findByUserIdAndBookId(int userId, int bookId);

    @Query("SELECT COUNT(ub) FROM UserBook ub WHERE ub.user.id = :userId AND ub.status = :status")
    Long quantityBooksWithStatus(@Param("userId") int userId, @Param("status") BookStatus status);

    @Query("SELECT ub.book FROM UserBook ub WHERE ub.user.id = :userId AND ub.status = :status")
    List<Book> findBooksByBookStatusAndUserId(int userId, BookStatus status);

    @Query("SELECT ub.book FROM UserBook ub WHERE ub.user.id = :userId")
    List<Book> findBooksByUserId(int userId);

    @Query("SELECT ub FROM UserBook ub WHERE ub.user.id = :userId AND ub.book.id = :bookId")
    Optional<UserBook> findBookByUserIdAndBookId(int userId, int bookId);

    @Query("SELECT ub.book.genre FROM UserBook ub WHERE ub.user.id = :userId " +
            "GROUP BY ub.book.genre ORDER BY COUNT(ub.book.genre) DESC")
    Page<BookGenre> findTopGenresByUserId(@Param("userId") int userId, Pageable pageable);
}
