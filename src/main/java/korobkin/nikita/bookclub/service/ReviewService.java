package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.entity.Book;
import korobkin.nikita.bookclub.entity.Review;
import korobkin.nikita.bookclub.repository.BookRepository;
import korobkin.nikita.bookclub.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public void createReview(Review review) {
        // Сохраняем отзыв
        reviewRepository.save(review);

        // Получаем книгу из отзыва
        Book book = review.getBook();

        // Пересчитываем средний рейтинг через запрос к базе
        float averageRating = reviewRepository.calculateAverageRatingForBook(book.getId());
        book.setRating(averageRating);

        // Обновляем поле updated_at
        book.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }
}
