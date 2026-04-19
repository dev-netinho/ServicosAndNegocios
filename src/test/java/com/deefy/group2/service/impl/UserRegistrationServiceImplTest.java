package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.UserRegistrationRequest;
import com.deefy.group2.exception.EmailJaCadastradoException;
import com.deefy.group2.model.Perfil;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.PerfilRepository;
import com.deefy.group2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private PasswordEncoder passwordEncoder; // Simula o "moedor" de senhas (BCrypt)

    @InjectMocks
    private UserRegistrationServiceImpl registrationService;

    @Test
    void deveRegistrarUsuarioComSenhaCriptografada() {
        UserRegistrationRequest request = new UserRegistrationRequest("Saylon", "saylon@email.com", "123456");
        Perfil perfilFree = new Perfil("Free");

        // Configuramos as respostas do ambiente:
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // E-mail está livre
        when(perfilRepository.findByNome("Free")).thenReturn(Optional.of(perfilFree)); // Perfil Free existe

        // O encoder: se receber "123456", devolva a versão bagunçada (hash)
        when(passwordEncoder.encode("123456")).thenReturn("hash_seguro_8899");

        registrationService.registrar(request);

        // VERIFICAÇÃO CRÍTICA: O método save foi chamado?
        // Usando 'any(User.class)' para conferir se um objeto do tipo User foi enviado ao banco.
        verify(userRepository, times(1)).save(any(User.class));

        // VERIFICAÇÃO DE SEGURANÇA: O serviço realmente usou o PasswordEncoder?
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void naoDeveSalvarSeEmailJaExistir() {
        UserRegistrationRequest request = new UserRegistrationRequest("Saylon", "saylon@email.com", "123456");

        // Simualando que o banco JÁ TEM esse e-mail
        when(userRepository.findByEmail("saylon@email.com")).thenReturn(Optional.of(new User()));

        // Verificamos se o sistema lança a nossa exceção customizada
        assertThrows(EmailJaCadastradoException.class, () -> registrationService.registrar(request));

        // Garantimos que, em caso de erro, o banco NUNCA foi chamado para salvar
        verify(userRepository, never()).save(any());
    }
}