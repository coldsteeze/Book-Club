package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.AuthenticationDto;
import korobkin.nikita.bookclub.dto.JwtResponseDto;
import korobkin.nikita.bookclub.dto.UserDto;
import korobkin.nikita.bookclub.service.AuthService;
import korobkin.nikita.bookclub.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(@RequestBody @Valid UserDto userDto) {
        log.info("Attempting to register a new user with username: {}", userDto.getUsername());
        String token = userService.register(userDto);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        log.info("User login attempt with username: {}", authenticationDto.getUsername());
        String token = authService.authenticate(authenticationDto);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }
}
