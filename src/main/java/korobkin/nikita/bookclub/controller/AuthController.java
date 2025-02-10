package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.AuthenticationDTO;
import korobkin.nikita.bookclub.dto.JwtResponseDTO;
import korobkin.nikita.bookclub.dto.UserDTO;
import korobkin.nikita.bookclub.service.AuthService;
import korobkin.nikita.bookclub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody @Valid UserDTO userDTO) {
        String token = userService.register(userDTO);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        String token = authService.authenticate(authenticationDTO);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
