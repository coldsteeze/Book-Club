package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.ReviewDto;
import korobkin.nikita.bookclub.dto.UpdateReviewDto;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody @Valid ReviewDto reviewDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is adding a review: {}", userDetails.getUsername(), reviewDto.getText());
        reviewService.createReview(reviewDto, userDetails.getUser());
        return ResponseEntity.ok("Review successfully added");
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@RequestBody @Valid UpdateReviewDto updateReviewDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable int reviewId) {
        log.info("User '{}' is updating review with ID {}: {}", userDetails.getUsername(), reviewId, updateReviewDto.getText());
        reviewService.updateReview(reviewId, updateReviewDto, userDetails.getUser());
        return ResponseEntity.ok("Review successfully updated");
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is deleting review with ID {}", userDetails.getUsername(), reviewId);
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok("Review successfully deleted");
    }
}

