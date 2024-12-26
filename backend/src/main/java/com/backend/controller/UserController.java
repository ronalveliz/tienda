package com.backend.controller;

import ch.qos.logback.core.subst.Token;
import com.backend.dto.NuevoUsuarioRegister;
import com.backend.dto.TokenClas;
import com.backend.model.RolName;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.security.JwtTokenUtils;
import com.backend.service.FileService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin("*")
@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository repo;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("user")
    public List<User> findAll(){

        return repo.findAll();
    }

    @PostMapping("users/register")
    public void register(@RequestBody NuevoUsuarioRegister register) {

        if (this.repo.existsByEmail(register.email())){
            throw new BadCredentialsException("Email ocupado. Elija otro email.");
        }

        User user = User.builder()
                .email(register.email())
                .password(passwordEncoder.encode(register.password()))
                .rolName(RolName.ROL_USER)
                .firtsName(register.firstName())
                .lastName(register.lastName())
                .phone(register.phone())
                .PhotoUrl(register.imgUser())
                .build();
        this.repo.save(user);
    }

    @PostMapping("users/login")
    public TokenClas login(@RequestBody NuevoUsuarioRegister login) {
        JwtTokenUtils.getCurrentUser().ifPresent(System.out::println);
        if (!this.repo.existsByEmail(login.email())) {
            throw new NoSuchElementException("User not found");
        }

        User user = this.repo.findByEmail(login.email()).orElseThrow();

        //if (!passwordEncoder.matches(login.password(), user.getPassword()))
        boolean correctPassword = passwordEncoder.matches(login.password(), user.getPassword());
        boolean incorrectPassword = !correctPassword;
        if (incorrectPassword){
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        // JWT Json Web TokenClas: jwt.io
        // Generar token de acceso: eyJhbGciOiJIUzI1NiIsIn......
        // Generar el token: https://github.com/jwtk/jjwt?tab=readme-ov-file#creating-a-jwt
        Date issuedDate = new Date();
        long nextWeekMillis = TimeUnit.DAYS.toMillis(7);
        Date expirationDate = new Date(issuedDate.getTime() + nextWeekMillis);
        byte[] key = Base64.getDecoder().decode("FZD5maIaX04mYCwsgckoBh1NJp6T3t62h2MVyEtdo3w="); // Clave secreta

        String token = Jwts.builder()
                // id del usuario
                .subject(String.valueOf(user.getId()))
                // La clave secreta para firmar el token y saber que es nuestro cuando lleguen las peticiones del frontend
                .signWith(Keys.hmacShaKeyFor(key))
                // Fecha emisión del token
                .issuedAt(issuedDate)
                // Fecha de expiración del token
                .expiration(expirationDate)
                // información personalizada: rol, username, email, avatar...
                .claim("roleName", user.getRolName())
                .claim("email", user.getEmail())
                //.claim("avatar", user.getAvatarUrl())
                // Construye el token
                .compact();

        return new TokenClas(token);
    }
    // Get account
    @GetMapping("user/account")
    public User getCurrentUser() {
        return JwtTokenUtils.getCurrentUser().orElseThrow();
    }


    @PutMapping("users/account")
    public User update(@RequestBody User user) {
        // Si está autenticado, y el usuario autenticado es ADMIN o es el mismo usuario que la variable user
        // entonces actualizar, en caso contrario no actualizamos
        JwtTokenUtils.getCurrentUser().ifPresent(currentUser -> {
            if (currentUser.getRolName() == RolName.ROL_ADMIN|| Objects.equals(currentUser.getId(), user.getId())) {
                this.repo.save(user);
            } else {
                throw new RuntimeException("No puede actualizar"); // Reemplazar por Excepción personalizada
            }
        });

        return user;
    }

    // subir avatar
    @PostMapping("users/account/avatar")
    public User uploadAvatar(
            @RequestParam(value = "photo") MultipartFile file
    ) {

        User user = JwtTokenUtils.getCurrentUser().orElseThrow();

        if (file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);
            user.setPhotoUrl(fileName);
            this.repo.save(user);
        }

        return user;
    }
    // subir avatar
    @PutMapping ("user/{id}")
    private ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id){

        if (user.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        User saveList = repo.save(user);
        return ResponseEntity.ok(saveList);
    }


}
