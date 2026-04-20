package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.LoginRequest;
import com.deefy.group2.dto.response.LoginResponse;
import com.deefy.group2.exception.CredenciaisInvalidasException;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.security.JwtService;
import com.deefy.group2.service.UserAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService; //Gerador de tokens
    private final AuthenticationManager authenticationManager; //O "validador" oficial

    public UserAuthenticationServiceImpl(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public LoginResponse login(LoginRequest request) {
        //O Spring tenta autenticar (compara o e-mail e a senha criptografada no banco)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        //Passou login, buscar user real por email.
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new CredenciaisInvalidasException("Usuário não encontrado após autenticação!"));

        //Geramos o Token Real usando o JwtService
        String token = jwtService.generateToken(user);

        //Identificação do Perfil para o retorno
       // String perfil = (user.getPerfil() != null) ? user.getPerfil().getNome() : "Sem Perfil";

        // Retorna o DTO com um token simbólico para a Sprint 1
        return new LoginResponse(token,
                "Bearer",
                user.getName(),
                user.getEmail()
        );
    }
}
