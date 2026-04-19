package com.deefy.group2.security.filter; // Verifique se o caminho bate com o seu

import com.deefy.group2.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //Procurar por "Authorization" na requisição
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        //Se não tiver o "Bearer ", o segurança ignora e passa para o próximo
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Pula o texto "Bearer " (7 caracteres) e pega só o Token
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // Usa seu JwtService para ler o e-mail

        //Se o e-mail for válido e o usuário ainda não estiver logado no sistema
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Busca os detalhes do usuário no banco
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //O token bate com o usuário e não expirou?
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Cria o "Crachá de Acesso" oficial do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Adiciona informações extras no crachá
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Avisa ao Spring: "Este usuário está oficialmente logado!"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Envia a requisição para o próximo filtro ou para o Controller
        filterChain.doFilter(request, response);
    }
}
