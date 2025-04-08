package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.ReviewDto;
import korobkin.nikita.bookclub.dto.UpdateReviewDto;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.Review;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.exception.BookDoesNotExistsException;
import korobkin.nikita.bookclub.exception.ReviewAlreadyExistsException;
import korobkin.nikita.bookclub.exception.ReviewDoesNotExistsException;
import korobkin.nikita.bookclub.repository.BookRepository;
import korobkin.nikita.bookclub.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public void createReview(ReviewDto reviewDto, User user) {
        log.info("User '{}' is creating review for book ID {}: {}",
                user.getUsername(), reviewDto.getBookId(), reviewDto.getText());

        Book book = bookRepository.findById(reviewDto.getBookId())
                .orElseThrow(() -> new BookDoesNotExistsException("Book doesn't exist"));

        if (reviewRepository.existsByBookIdAndUserId(book.getId(), user.getId())) {
            log.warn("User '{}' already has a review for book with ID {}: {}",
                    user.getUsername(), book.getId(), reviewDto.getText());
            throw new ReviewAlreadyExistsException("Review for this book already exists");
        }

        Review review = new Review();
        review.setText(reviewDto.getText());
        review.setRating(reviewDto.getRating());
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);

        Float averageRating = reviewRepository.calculateAverageRatingForBook(book.getId());
        book.setRating(averageRating);
        book.setUpdatedAt(LocalDateTime.now());

        bookRepository.save(book);

        log.info("Review created successfully for book with ID {} by user '{}'", book.getId(), user.getUsername());
    }

    public void updateReview(int reviewId, UpdateReviewDto updateReviewDto, User user) {
        log.info("User '{}' is updating review with ID {}", user.getUsername(), reviewId);

        Review review = reviewRepository.findByIdAndUserId(reviewId, user.getId())
                .orElseThrow(() -> new ReviewDoesNotExistsException("Review doesn't exist"));

        if (StringUtils.hasText(updateReviewDto.getText())) {
            review.setText(updateReviewDto.getText());
        }
        if (updateReviewDto.getRating() != null) {
            review.setRating(updateReviewDto.getRating());
        }

        reviewRepository.save(review);

        log.info("Review with ID {} updated successfully for user '{}'", reviewId, user.getUsername());
    }

    public void deleteReview(int reviewId, User user) {
        log.info("User '{}' is deleting review with ID {}", user.getUsername(), reviewId);

        Review review = reviewRepository.findByIdAndUserId(reviewId, user.getId())
                .orElseThrow(() -> new ReviewDoesNotExistsException("Review doesn't exist"));

        reviewRepository.delete(review);

        log.info("Review with ID {} deleted successfully by user '{}'", reviewId, user.getUsername());
    }
}

