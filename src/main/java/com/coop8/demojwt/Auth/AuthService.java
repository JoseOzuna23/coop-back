package com.coop8.demojwt.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coop8.demojwt.Models.Usuarios;
import com.coop8.demojwt.Repository.UsuariosRepository;
import com.coop8.demojwt.Request.LoginRequest;
import com.coop8.demojwt.Request.RegisterRequest;
import com.coop8.demojwt.Jwt.JwtService;
import com.coop8.demojwt.Models.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuariosRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

    public AuthResponse register(RegisterRequest request) {
        Usuarios usuarios = Usuarios.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode( request.getPassword()))
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .role(Role.USER)
            .build();

        userRepository.save(usuarios);

        return AuthResponse.builder()
            .token(jwtService.getToken(usuarios))
            .build();
        
    }

}
