package com.nextar.calculator.services;

import com.nextar.calculator.models.*;
import com.nextar.calculator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPassword(),
                        request.getUsername()
                )
        );}catch (Exception e){
            System.out.println(e.getStackTrace());
        }

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
