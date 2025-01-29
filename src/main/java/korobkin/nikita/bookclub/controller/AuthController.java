package korobkin.nikita.bookclub.controller;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.AuthenticationDTO;
import korobkin.nikita.bookclub.dto.UserDTO;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.security.JWTUtil;
import korobkin.nikita.bookclub.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO) {
        User user = convertUserDTOToUser(userDTO);
        userService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok("jwt-token: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationDTO AuthenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(AuthenticationDTO.getUsername(),
                        AuthenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Bad credentials");
        }

        String token = jwtUtil.generateToken(authenticationToken.getPrincipal().toString());
        return ResponseEntity.ok("jwt-token: " + token);
    }

    private User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
