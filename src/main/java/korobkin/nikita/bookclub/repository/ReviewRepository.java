package korobkin.nikita.bookclub.repository;

import korobkin.nikita.bookclub.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Float calculateAverageRatingForBook(@Param("bookId") int bookId);

    Boolean existsByBookIdAndUserId(int bookId, int userId);

    int countReviewsByUserId(int userId);

    Optional<Review> findByBookIdAndUserId(int bookId, int userId);

    Optional<Review> findByIdAndUserId(int id, int userId);
}
