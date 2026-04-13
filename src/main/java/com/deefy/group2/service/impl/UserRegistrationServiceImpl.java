package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.UserRegistrationRequest;
import com.deefy.group2.exception.EmailJaCadastradoException;
import com.deefy.group2.model.Perfil;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.PerfilRepository;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.service.UserRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final PerfilRepository perfilRepository; // Adicionamos a dependência

    public UserRegistrationServiceImpl(UserRepository userRepository, PerfilRepository perfilRepository) {
        this.userRepository = userRepository;
        this.perfilRepository = perfilRepository;
    }

    @Override
    @Transactional
    public void registrar(UserRegistrationRequest request) {
        //Verificação de Duplicidade: Garante que não existam dois usuários com o mesmo e-mail
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailJaCadastradoException("Este e-mail já está cadastrado!");
        }

        //Atribuição de Perfil: Busca o perfil 'Free' no banco para novos usuários
        Perfil perfilPadrao = perfilRepository.findByNome("Free")
                .orElseThrow(() -> new RuntimeException("Erro: Perfil padrão 'Free' não configurado."));

        //Cria a entidade User associando os dados recebidos e o perfil padrão
        User newUser = new User(
                request.name(),
                request.email(),
                request.password(),
                perfilPadrao
        );

        //Registra o novo usuário no banco de dados (PostgreSQL/Supabase)
        userRepository.save(newUser);
    }
}