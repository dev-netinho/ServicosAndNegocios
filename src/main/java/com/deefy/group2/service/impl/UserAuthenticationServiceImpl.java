package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.LoginRequest;
import com.deefy.group2.dto.response.LoginResponse;
import com.deefy.group2.exception.CredenciaisInvalidasException;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.service.UserAuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserRepository userRepository;

    public UserAuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Busca o usuário pelo e-mail único definido no banco
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos!"));

        // Comparação de senha (por enquanto manual, até adicionarmos o encoder)
        if (!user.getPassword().equals(request.password())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos!");
        }

        // Identificação do Perfil para o retorno da API
        String perfil = (user.getPerfil() != null) ? user.getPerfil().getNome() : "Sem Perfil";

        // Retorna o DTO com um token simbólico para a Sprint 1
        return new LoginResponse("token-provisorio-sprint1", "Bearer", user.getName() + " [" + perfil + "]");
    }
}