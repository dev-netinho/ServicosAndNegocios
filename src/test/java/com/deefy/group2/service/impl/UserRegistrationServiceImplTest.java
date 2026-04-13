package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.UserRegistrationRequest;
import com.deefy.group2.exception.EmailJaCadastradoException;
import com.deefy.group2.model.Perfil;
import com.deefy.group2.repository.PerfilRepository;
import com.deefy.group2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Inicializa o Mockito para esta classe
class UserRegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository; // Simula o banco de usuários

    @Mock
    private PerfilRepository perfilRepository; // Simula o banco de perfis

    @InjectMocks
    private UserRegistrationServiceImpl registrationService; // Injeção

    @Test
    void deveRegistrarUsuarioComSucesso() {
        //Cenário (Given)
        UserRegistrationRequest request = new UserRegistrationRequest("Saylon", "saylon@email.com", "123456");
        Perfil perfilFree = new Perfil("Free"); // Conforme o script do banco

        // Simulamos que o e-mail não existe e que o perfil 'Free' existe
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(perfilRepository.findByNome("Free")).thenReturn(Optional.of(perfilFree));

        // Ação
        registrationService.registrar(request);

        //Verificação: // Garante que o método save foi chamado exatamente uma vez
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        //Simulamos que o e-mail já está no banco
        UserRegistrationRequest request = new UserRegistrationRequest("Saylon", "saylon@email.com", "123456");
        when(userRepository.findByEmail("saylon@email.com")).thenReturn(Optional.of(mock(com.deefy.group2.model.User.class)));

        //Verificar se dispara a nossa exceção customizada
        assertThrows(EmailJaCadastradoException.class, () -> registrationService.registrar(request));

        //Garante que o sistema parou e NUNCA tentou salvar no banco
        verify(userRepository, never()).save(any());
    }
}
