package com.deefy.group2.service;

public interface AuthService {

    String autenticar(String email, String senha);

    boolean usuarioAutenticado(String usuarioId);

    boolean usuarioAdministrador(String usuarioId);
}
