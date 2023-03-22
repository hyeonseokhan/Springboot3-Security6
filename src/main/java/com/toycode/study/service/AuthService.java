package com.toycode.study.service;

import com.toycode.study.dto.AuthenticationRequest;
import com.toycode.study.dto.AuthenticationResponse;
import com.toycode.study.dto.RegisterRequest;
import com.toycode.study.entity.Role;
import com.toycode.study.entity.Token;
import com.toycode.study.entity.TokenType;
import com.toycode.study.entity.User;
import com.toycode.study.jwt.TokenProvider;
import com.toycode.study.repository.TokenRepository;
import com.toycode.study.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .nickname(request.getNickname())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        User savedUser = userRepository.save(user);
        String jwtToken = tokenProvider.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow();
        String jwtToken = tokenProvider.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
