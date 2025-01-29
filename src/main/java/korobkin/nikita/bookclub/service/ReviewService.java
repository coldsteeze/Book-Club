package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.Review;
import korobkin.nikita.bookclub.repository.BookRepository;
import korobkin.nikita.bookclub.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public void createReview(Review review) {
        Book book = bookRepository.findById(review.getBook().getId()).orElse(null);
        review.setBook(book);
        reviewRepository.save(review);
        Float averageRating = reviewRepository.calculateAverageRatingForBook(book.getId());
        book.setRating(averageRating);
        book.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }
}
