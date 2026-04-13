package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.LoginRequest;
import com.deefy.group2.dto.response.LoginResponse;
import com.deefy.group2.exception.CredenciaisInvalidasException;
import com.deefy.group2.model.Perfil;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthenticationServiceImpl authService;

    @Test
    @DisplayName("Deve autenticar com sucesso quando as credenciais forem válidas")
    void deveAutenticarComSucesso() {
        // Preparação do cenário com um usuário existente no banco
        Perfil perfilPremium = new Perfil("Premium"); // Nome conforme o script do banco
        User usuarioValido = new User("João Souza", "joao@email.com", "123456", perfilPremium);

        // Simulamos que o repositório encontra o e-mail informado
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuarioValido));
        LoginRequest request = new LoginRequest("joao@email.com", "123456");

        // Execução da lógica de login
        LoginResponse response = authService.login(request);

        // Validação do token e dados do perfil retornados
        assertThat(response.token()).isEqualTo("token-provisorio-sprint1");
        assertThat(response.username()).isEqualTo("João Souza [Premium]");
        verify(userRepository, times(1)).findByEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail não existir no banco")
    void deveLancarExcecaoQuandoEmailNaoExistir() {
        //O repositório retorna vazio para um e-mail não cadastrado
        when(userRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest("inexistente@email.com", "123456");

        // Verificamos se a exceção correta é disparada e a mensagem de segurança
        CredenciaisInvalidasException exception = assertThrows(CredenciaisInvalidasException.class, () -> {
            authService.login(request);
        });

        assertThat(exception.getMessage()).isEqualTo("E-mail ou senha inválidos!");
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha estiver incorreta")
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        // Usuário existe, mas a senha enviada é diferente da armazenada
        Perfil perfilFree = new Perfil("Free");
        User usuarioNoBanco = new User("Maria Oliveira", "maria@email.com", "senha_correta", perfilFree);

        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuarioNoBanco));
        LoginRequest request = new LoginRequest("maria@email.com", "senha_errada");

        //O sistema deve barrar o acesso mesmo com e-mail correto
        CredenciaisInvalidasException exception = assertThrows(CredenciaisInvalidasException.class, () -> {
            authService.login(request);
        });

        assertThat(exception.getMessage()).isEqualTo("E-mail ou senha inválidos!");
    }
}