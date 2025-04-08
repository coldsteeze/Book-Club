package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.AuthenticationDto;
import korobkin.nikita.bookclub.exception.AuthenticationFailedException;
import korobkin.nikita.bookclub.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String authenticate(AuthenticationDto authenticationDTO) {
        log.debug("Authenticating user with the username {}", authenticationDTO.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        log.info("User {} Authentication successful", authenticationDTO.getUsername());
        return jwtUtil.generateToken(authenticationToken.getPrincipal().toString());
    }
}
