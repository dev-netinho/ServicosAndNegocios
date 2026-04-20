package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.UserRegistrationRequest;
import com.deefy.group2.exception.EmailJaCadastradoException;
import com.deefy.group2.model.Perfil;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.PerfilRepository;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.service.UserRegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(
            UserRepository userRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void registrar(UserRegistrationRequest request) {
        //Verificação de Duplicidade: Garante que não existam dois usuários com o mesmo e-mail
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailJaCadastradoException("Este e-mail já está cadastrado!");
        }

        // Adapta o cadastro para os perfis já existentes nos bancos conhecidos do projeto.
        Perfil perfilPadrao = perfilRepository.findByNome("Ouvinte")
                .or(() -> perfilRepository.findByNome("Free"))
                .orElseThrow(() -> new RuntimeException("Erro: perfil padrão de usuário não configurado."));

        //Cria a entidade User associando os dados recebidos e o perfil padrão
        User newUser = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                perfilPadrao);

        //Registra o novo usuário no banco de dados (PostgreSQL/Supabase)
        userRepository.save(newUser);
    }
}
