package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.ReviewDTO;
import korobkin.nikita.bookclub.dto.UpdateReviewDTO;
import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.Review;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.exception.BookDoesNotExistsException;
import korobkin.nikita.bookclub.exception.ReviewAlreadyExistsException;
import korobkin.nikita.bookclub.exception.ReviewDoesNotExistsException;
import korobkin.nikita.bookclub.repository.BookRepository;
import korobkin.nikita.bookclub.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public void createReview(ReviewDTO reviewDTO, User user) {
        Book book = bookRepository.findById(reviewDTO.getBookId())
                .orElseThrow(() -> new BookDoesNotExistsException("Book doesn't exist"));

        if (reviewRepository.existsByBookIdAndUserId(book.getId(), user.getId())) {
            throw new ReviewAlreadyExistsException("Review for this book already exists");
        }

        Review review = new Review();
        review.setText(reviewDTO.getText());
        review.setRating(reviewDTO.getRating());
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);

        Float averageRating = reviewRepository.calculateAverageRatingForBook(book.getId());
        book.setRating(averageRating);
        book.setUpdatedAt(LocalDateTime.now());

        bookRepository.save(book);
    }

    public void updateReview(int reviewId, UpdateReviewDTO updateReviewDTO, User user) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, user.getId())
                .orElseThrow(() -> new ReviewDoesNotExistsException("Review doesn't exist"));
        if (StringUtils.hasText(updateReviewDTO.getText())) {
            review.setText(updateReviewDTO.getText());
        }
        if (updateReviewDTO.getRating() != null) {
            review.setRating(updateReviewDTO.getRating());
        }
        reviewRepository.save(review);
    }

    public void deleteReview(int reviewId, User user) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, user.getId())
                .orElseThrow(() -> new ReviewDoesNotExistsException("Review doesn't exist"));
        reviewRepository.delete(review);
    }
}
