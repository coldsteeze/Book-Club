package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.AuthenticationDTO;
import korobkin.nikita.bookclub.dto.UserDTO;
import korobkin.nikita.bookclub.exception.AuthenticationFailedException;
import korobkin.nikita.bookclub.security.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public String authenticate(AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        return jwtUtil.generateToken(authenticationToken.getPrincipal().toString());
    }
}
