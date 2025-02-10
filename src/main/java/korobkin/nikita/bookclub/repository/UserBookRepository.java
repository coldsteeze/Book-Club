package korobkin.nikita.bookclub.repository;


import korobkin.nikita.bookclub.entity.UserBook;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, Integer> {
    Optional<UserBook> findByUserIdAndBookId(int userId, int bookId);

    @Query("SELECT COUNT(ub) FROM UserBook ub WHERE ub.user.id = :userId AND ub.status = :status")
    Long quantityBooksWithStatus(@Param("userId") int userId, @Param("status") BookStatus status);
}
