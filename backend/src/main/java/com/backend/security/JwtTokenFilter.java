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
        // 1. Extraer de la cabecera Authorization de la request
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.hasLength(bearerToken) || !bearerToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = bearerToken.substring("Bearer ".length());

        // 2. Verificar el token JWT
        Optional<User> userOptional = validateTokenAndExtractUser(token);
        if (userOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 3. Cargar usuario en contexto de seguridad Spring
        User user = userOptional.get();
        SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRolName().toString());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of(role));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }


    private Optional<User> validateTokenAndExtractUser(String token) {
        byte[] key = Base64.getDecoder().decode("FZD5maIaX04mYCwsgckoBh1NJp6T3t62h2MVyEtdo3w=");
        try {
            String userId = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return this.userRepository.findById(Long.valueOf(userId));
        } catch (JwtException e) {
            log.error("Error en la validación del token JWT");
            return Optional.empty();
        }
    }
}
