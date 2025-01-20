package korobkin.nikita.bookclub.repository;

import korobkin.nikita.bookclub.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    float calculateAverageRatingForBook(@Param("bookId") int bookId);
}
