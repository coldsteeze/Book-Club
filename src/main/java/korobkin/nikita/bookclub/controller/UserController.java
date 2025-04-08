package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.UpdatePasswordDto;
import korobkin.nikita.bookclub.dto.UpdateUsernameDto;
import korobkin.nikita.bookclub.dto.UserProfileDto;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Fetching profile for user '{}'", userDetails.getUsername());
        UserProfileDto userProfileDto = userService.getUserProfile(userDetails.getUser());
        return userProfileDto != null ? ResponseEntity.ok(userProfileDto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/username")
    public ResponseEntity<String> updateProfileUsername(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody @Valid UpdateUsernameDto updateUsernameDto) {
        log.info("User '{}' is updating their username to '{}'",
                userDetails.getUsername(), updateUsernameDto.getUsername());
        userService.updateProfileUsername(updateUsernameDto.getUsername(), userDetails.getUser());
        SecurityContextHolder.clearContext();
        log.info("Username changed for user '{}'. User needs to re-login.", userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Token-Expired", "true")
                .body("Username changed. Please re-login.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> updateProfilePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        log.info("User '{}' is changing their password", userDetails.getUsername());
        userService.updateProfilePassword(updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                userDetails.getUser());
        SecurityContextHolder.clearContext();
        log.info("Password changed for user '{}'. User needs to re-login.", userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Token-Expired", "true")
                .body("Password changed. Please re-login.");
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is deleting their profile", userDetails.getUsername());
        userService.deleteProfile(userDetails.getUser());
        SecurityContextHolder.clearContext();
        log.info("Profile of user '{}' deleted successfully", userDetails.getUsername());
        return ResponseEntity.ok("Profile deleted successfully");
    }
}
