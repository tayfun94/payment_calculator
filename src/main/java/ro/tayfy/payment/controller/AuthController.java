package ro.tayfy.payment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.tayfy.payment.dto.UserRequestDTO;
import ro.tayfy.payment.security.JwtUtil;
import ro.tayfy.payment.service.UserService;

import java.time.Duration;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO authRequestDTO, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        String token = jwtUtil.generateToken(authRequestDTO.getUsername());
        Date expiryTime = jwtUtil.getTokenExpiration(token);
        generateCookies(response, token, expiryTime);
        return ResponseEntity.ok("Login successful");
    }

    private static void generateCookies(HttpServletResponse response, String token, Date expiryTime) {
        Cookie authTokenCookie = new Cookie("AuthToken", token);
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(false); //true
        authTokenCookie.setPath("/");
        authTokenCookie.setMaxAge((int) Duration.ofDays(1).toSeconds());

        Cookie expiryTimeCookie = new Cookie("expiryTime", String.valueOf(expiryTime.getTime()));
        expiryTimeCookie.setHttpOnly(false);
        expiryTimeCookie.setSecure(false); //true
        expiryTimeCookie.setPath("/");
        expiryTimeCookie.setMaxAge((int) Duration.ofDays(1).toSeconds());

        response.addCookie(authTokenCookie);
        response.addCookie(expiryTimeCookie);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO authRequestDTO) {
        userService.registerUser(authRequestDTO.getUsername(), authRequestDTO.getPassword(), "USER");
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie authTokenCookie = new Cookie("AuthToken", null);
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setSecure(true);
        authTokenCookie.setPath("/");
        authTokenCookie.setMaxAge(0);
        response.addCookie(authTokenCookie);
        return ResponseEntity.ok("Logged out successfully");
    }
}
