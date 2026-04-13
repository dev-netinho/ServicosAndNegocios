package com.deefy.group2.controller;

import com.deefy.group2.dto.request.LoginRequest;
import com.deefy.group2.dto.request.UserRegistrationRequest;
import com.deefy.group2.dto.response.LoginResponse;
import com.deefy.group2.service.UserAuthenticationService;
import com.deefy.group2.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserAuthenticationService authService;
    private final UserRegistrationService registrationService;

    // Injeção de dependência dos dois serviços específicos
    public AuthController(UserAuthenticationService authService,
                          UserRegistrationService registrationService) {
        this.authService = authService;
        this.registrationService = registrationService;
    }

    //Endpoint para criação de novos usuários com os campos DTO
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody UserRegistrationRequest request) {
        registrationService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    //Endpoint de autenticação que processa as credenciais e retorna o token de acesso
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}