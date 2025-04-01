package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.ReviewDto;
import korobkin.nikita.bookclub.dto.UpdateReviewDto;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody @Valid ReviewDto reviewDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.createReview(reviewDto, userDetails.getUser());
        return ResponseEntity.ok("Review successfully added");
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@RequestBody @Valid UpdateReviewDto updateReviewDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable int reviewId) {
        reviewService.updateReview(reviewId, updateReviewDto, userDetails.getUser());
        return ResponseEntity.ok("Review successfully updated");
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok("Review successfully deleted");
    }
}
