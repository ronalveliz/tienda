package com.backend.security;

import com.backend.model.User;
import com.backend.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Ejecutando");


        //extraer el token de la cabecera Authorization de la request

        String bearerToken = request.getHeader("Authorization");
        String token = "";

        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith("Bearer")){

            token = bearerToken.substring("Bearer ".length());
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Extraido token JWT {}", token);

        // verificar el token

        byte[] key = Base64.getDecoder().decode("4PWbGp0oV5si8hXJS0Hl/yk9RWX7SZK7DdckNx3e0cQ=");

        String userId = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(key))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        log.info(userId);

        // obtener el usuario cuyo id hemos extraido del jwt

        Optional<User> userOptional = this.userRepository.findById(Long.valueOf(userId));

        if (userOptional.isEmpty()){
            log.warn("Usuario con Id no encontrado", userId);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Crear objeto autenticacion con datos del usuario y guardarlo en el contexto de seguridad de spring Security

        User user = userOptional.get();
        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + user.getRolName().toString());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,null, List.of(role));
        SecurityContextHolder.getContext().setAuthentication(auth);

        //Dejar pasar la peticion


        filterChain.doFilter(request,response);
    }
}
