package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.UpdatePasswordDto;
import korobkin.nikita.bookclub.dto.UpdateUsernameDto;
import korobkin.nikita.bookclub.dto.UserProfileDto;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileDto userProfileDto = userService.getUserProfile(userDetails.getUser());
        return userProfileDto != null ? ResponseEntity.ok(userProfileDto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/username")
    public ResponseEntity<String> updateProfileUsername(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody @Valid UpdateUsernameDto updateUsernameDto) {
        userService.updateProfileUsername(updateUsernameDto.getUsername(), userDetails.getUser());
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Token-Expired", "true")
                .body("Username changed. Please re-login.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> updateProfilePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        userService.updateProfilePassword(updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                userDetails.getUser());
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Token-Expired", "true")
                .body("Password changed. Please re-login.");
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteProfile(userDetails.getUser());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Profile deleted successfully");
    }
}
