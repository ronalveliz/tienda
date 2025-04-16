package com.backend.controller;

import com.backend.dto.LoginUsuario;
import com.backend.dto.NuevoUsuarioRegister;
import com.backend.dto.Token;
import com.backend.model.RolName;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.security.JwtTokenUtils;
import com.backend.service.FileService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
                .firstName(register.firstName())
                .lastName(register.lastName())
                .phone(register.phone())
                .rolName(register.roleName()).rolName(RolName.USER)
                .imgUser(register.imgUser()).imgUser("https://www.pngkey.com/png/detail/230-2301779_best-classified-apps-default-user-profile.png")
                .build();
        this.repo.save(user);
    }

    @PostMapping("/users/login")
    public Token login(@RequestBody LoginUsuario login) {
        JwtTokenUtils.getCurrentUser().ifPresent(System.out::println);

        if (!repo.existsByEmail(login.email())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        User user = repo.findByEmail(login.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if(!passwordEncoder.matches(login.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }
        Date issuedDate = new Date();
        long nextWeekMillis = TimeUnit.DAYS.toMillis(7);
        Date expirationDate = new Date(issuedDate.getTime() + nextWeekMillis);

        byte[] key = Base64.getDecoder().decode("4PWbGp0oV5si8hXJS0Hl/yk9RWX7SZK7DdckNx3e0cQ=");

        String token = Jwts.builder()
                // id del usuario
                .subject(String.valueOf(user.getId()))
                // La clave secreta para firmar el token y saber que es nuestro cuando lleguen las peticiones del frontend
                .signWith(Keys.hmacShaKeyFor(key))
                // Fecha emisión del token
                .issuedAt(issuedDate)
                // Fecha de expiración del token
                .expiration(expirationDate)
                // información personalizada: rol, username, email...
                .claim("rolname", user.getRolName())
                .claim("email", user.getEmail())
                // Construye el token
                .compact();
        return ResponseEntity.ok(new Token(token)).getBody();


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
            if (currentUser.getRolName() == RolName.ADMIN|| Objects.equals(currentUser.getId(), user.getId())) {
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
            user.setImgUser(fileName);
            this.repo.save(user);
        }

        return user;
    }
    // subir avatar
    @PutMapping ("user/{id}")
    private ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id){
        Optional<User> userOtp = repo.findById(id);
        if (user.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        User usuariosFromDB = userOtp.get();
        // faltan mas atributos
        return ResponseEntity.ok(repo.save(usuariosFromDB));
    }


    @DeleteMapping("user/id")
    private ResponseEntity<Void> deleteById(@PathVariable Long id){

        repo.deleteById(id);
        return ResponseEntity.noContent().build(); //204
    }

}
