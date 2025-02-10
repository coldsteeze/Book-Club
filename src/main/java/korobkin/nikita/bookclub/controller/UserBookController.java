package korobkin.nikita.bookclub.controller;

import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import korobkin.nikita.bookclub.security.CustomUserDetails;
import korobkin.nikita.bookclub.service.UserBookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-books")
@AllArgsConstructor
public class UserBookController {
    private final UserBookService userBookService;

    @PostMapping("/add")
    public ResponseEntity<String> addBookToUser(@RequestParam int bookId,
                                                @RequestParam BookStatus bookStatus,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        userBookService.addBookToUser(user.getId(), bookId, bookStatus);
        return ResponseEntity.ok("Book added successfully");
    }
}
