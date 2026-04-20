package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.LoginRequest;
import com.deefy.group2.dto.response.LoginResponse;
import com.deefy.group2.exception.CredenciaisInvalidasException;
import com.deefy.group2.model.Perfil;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository; // Simula o banco de dados

    @Mock
    private JwtService jwtService; // Simula o gerador de tokens JWT

    @Mock
    private AuthenticationManager authenticationManager; // Simula o validador de senhas do Spring

    @InjectMocks
    private UserAuthenticationServiceImpl authService; // A classe que estamos testando de verdade

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar o token real")
    void deveAutenticarComSucesso() {
        // Preparamos os dados de teste
        Perfil perfil = new Perfil("Free");
        User usuarioValido = new User("Saylon Batista", "saylon@email.com", "123456", perfil);
        LoginRequest request = new LoginRequest("saylon@email.com", "123456");


        // Quando o service procurar o e-mail, o banco responde com o usuário acima
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(usuarioValido));

        // Quando o service pedir para gerar um token, o JwtService responde com uma string fixa
        when(jwtService.generateToken(usuarioValido)).thenReturn("token-jwt-fake-123");

        // Executar a ação de login
        LoginResponse response = authService.login(request);

        // Verificamção dos resultados
        assertThat(response.token()).isEqualTo("token-jwt-fake-123");
        assertThat(response.name()).isEqualTo("Saylon Batista");

        // Verificando se o "Segurança" do Spring foi realmente chamado para validar a senha
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail não existir no banco")
    void deveLancarExcecaoQuandoEmailNaoExistir() {
        LoginRequest request = new LoginRequest("inexistente@email.com", "123456");

        // O AuthenticationManager falha se o UserDetailsService não achar o e-mail
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Usuário não encontrado"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));

        // Verifica se o Manager barrou, o código nem tentou buscar no Repository depois
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Deve falhar quando o AuthenticationManager barrar as credenciais")
    void deveFalharQuandoCredenciaisIncorretas() {

        LoginRequest request = new LoginRequest("usuario@email.com", "senha_errada");

        // Simulamos que o validador de senhas lançou um erro de "Credenciais Inválidas"
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Senha incorreta"));

        // Verificamos se o nosso serviço repassa esse erro corretamente
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
