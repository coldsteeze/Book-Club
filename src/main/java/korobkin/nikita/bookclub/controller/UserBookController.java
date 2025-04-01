package korobkin.nikita.bookclub.controller;

import korobkin.nikita.bookclub.dto.BookDto;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.UserBookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-books")
@AllArgsConstructor
public class UserBookController {
    private final UserBookService userBookService;

    @PostMapping("/add")
    public ResponseEntity<String> addBookToUser(@RequestParam int bookId,
                                                @RequestParam BookStatus bookStatus,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        userBookService.addBookToUser(user.getId(), bookId, bookStatus);
        return ResponseEntity.ok("Book added successfully");
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getUserBooks(@RequestParam(required = false) BookStatus bookStatus,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BookDto> bookDTOS = userBookService.getBooksByFilter(userDetails.getUser().getId(), bookStatus);
        return ResponseEntity.ok(bookDTOS);
    }

    @PatchMapping
    public ResponseEntity<String> updateBookStatus(@RequestParam int bookId,
                                                   @RequestParam BookStatus bookStatus,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userBookService.updateUserBookStatus(userDetails.getUser().getId(), bookId, bookStatus);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUserBook(@RequestParam int bookId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userBookService.deleteUserBook(userDetails.getUser().getId(), bookId);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
