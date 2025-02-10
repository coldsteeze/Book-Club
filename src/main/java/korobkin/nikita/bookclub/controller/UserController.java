package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.UpdatePasswordDTO;
import korobkin.nikita.bookclub.dto.UpdateUsernameDTO;
import korobkin.nikita.bookclub.dto.UserProfileDTO;
import korobkin.nikita.bookclub.security.CustomUserDetails;
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
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileDTO userProfileDTO = userService.getUserProfile(userDetails.getUser());
        return userProfileDTO != null ? ResponseEntity.ok(userProfileDTO) : ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/username")
    public ResponseEntity<String> updateProfileUsername(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody @Valid UpdateUsernameDTO updateUsernameDTO) {
        userService.updateProfileUsername(updateUsernameDTO.getUsername(), userDetails.getUser());
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Token-Expired", "true")
                .body("Username changed. Please re-login.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> updateProfilePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        userService.updateProfilePassword(updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword(),
                userDetails.getUser());
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Token-Expired", "true")
                .body("Password changed. Please re-login.");
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteProfile(userDetails.getUser());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Profile deleted successfully");
    }
}
