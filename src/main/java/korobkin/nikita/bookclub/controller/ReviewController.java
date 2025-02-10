package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.ReviewDTO;
import korobkin.nikita.bookclub.dto.UpdateReviewDTO;
import korobkin.nikita.bookclub.security.CustomUserDetails;
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
    public ResponseEntity<String> addReview(@RequestBody @Valid ReviewDTO reviewDTO,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reviewService.createReview(reviewDTO, customUserDetails.getUser());
        return ResponseEntity.ok("Review successfully added");
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@RequestBody @Valid UpdateReviewDTO updateReviewDTO,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                               @PathVariable int reviewId) {
        reviewService.updateReview(reviewId, updateReviewDTO, customUserDetails.getUser());
        return ResponseEntity.ok("Review successfully updated");
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reviewService.deleteReview(reviewId, customUserDetails.getUser());
        return ResponseEntity.ok("Review successfully deleted");
    }
}
