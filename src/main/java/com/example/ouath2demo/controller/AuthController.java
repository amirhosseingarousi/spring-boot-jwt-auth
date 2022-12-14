package com.example.ouath2demo.controller;

import com.example.ouath2demo.model.LoginResult;
import com.example.ouath2demo.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResult login(@RequestParam String username, @RequestParam String password) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(UNAUTHORIZED, "User not found");
        }

        if(passwordEncoder.matches(password, userDetails.getPassword())) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", username);
            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            claims.put("authorities", authorities);
            claims.put("userId", String.valueOf(1));

            String jwt = jwtHelper.createJwtForClaims(username, claims);
            return new LoginResult(jwt);
        }

        throw new ResponseStatusException(UNAUTHORIZED, "User not authenticated");
    }
}
