package korobkin.nikita.bookclub.controller;

import korobkin.nikita.bookclub.dto.BookDto;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import korobkin.nikita.bookclub.security.UserDetailsImpl;
import korobkin.nikita.bookclub.service.UserBookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user-books")
@AllArgsConstructor
public class UserBookController {
    private final UserBookService userBookService;

    @PostMapping("/add")
    public ResponseEntity<String> addBookToUser(@RequestParam int bookId,
                                                @RequestParam BookStatus bookStatus,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is adding book with ID {} and status '{}' to their collection",
                userDetails.getUsername(), bookId, bookStatus);
        userBookService.addBookToUser(userDetails.getUser().getId(), bookId, bookStatus);
        return ResponseEntity.ok("Book added successfully");
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getUserBooks(@RequestParam(required = false) BookStatus bookStatus,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is fetching their books with status '{}'",
                userDetails.getUsername(), bookStatus != null ? bookStatus : "ALL");
        List<BookDto> listBookDto = userBookService.getBooksByFilter(userDetails.getUser().getId(), bookStatus);
        return ResponseEntity.ok(listBookDto);
    }

    @PatchMapping
    public ResponseEntity<String> updateBookStatus(@RequestParam int bookId,
                                                   @RequestParam BookStatus bookStatus,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is updating the status of book with ID {} to '{}'",
                userDetails.getUsername(), bookId, bookStatus);
        userBookService.updateUserBookStatus(userDetails.getUser().getId(), bookId, bookStatus);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUserBook(@RequestParam int bookId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("User '{}' is deleting book with ID {} from their collection",
                userDetails.getUsername(), bookId);
        userBookService.deleteUserBook(userDetails.getUser().getId(), bookId);
        return ResponseEntity.ok("Book deleted successfully");
    }
}

