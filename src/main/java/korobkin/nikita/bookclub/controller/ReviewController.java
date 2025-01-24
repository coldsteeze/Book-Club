package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.entity.Review;
import korobkin.nikita.bookclub.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody @Valid Review review) {
        reviewService.createReview(review);
        return ResponseEntity.ok(review);
    }
}
