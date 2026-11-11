package com.enterpriseflow.auth;

import com.enterpriseflow.user.*;
import com.enterpriseflow.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AppUserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthController(AppUserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users=users; this.encoder=encoder; this.jwt=jwt;
    }
    public record RegisterRequest(@NotBlank String name, @Email @NotBlank String email,
                                  @Size(min=10) String password) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record TokenResponse(String accessToken, String tokenType) {}
    @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r) {
        if (users.existsByEmail(r.email().toLowerCase())) return ResponseEntity.status(409).body("Email already registered");
        users.save(new AppUser(r.name(), r.email().toLowerCase(), encoder.encode(r.password())));
        return ResponseEntity.status(201).body("Account created. Please log in.");
    }
    @PostMapping("/login") public TokenResponse login(@Valid @RequestBody LoginRequest r) {
        var user=users.findByEmail(r.email().toLowerCase()).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (!encoder.matches(r.password(), user.getPasswordHash()))
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        return new TokenResponse(jwt.issue(user.getEmail()), "Bearer");
    }
}
