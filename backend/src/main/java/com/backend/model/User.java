package com.backend.model;

import io.swagger.v3.oas.annotations.info.Contact;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "clientes")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String firstName;

    @Column(nullable = false)
    private  String lastName;

    @Column(nullable = false, unique = true)
    private  String email;

    private String password;

    private String phone;

   @Enumerated(EnumType.STRING)
    private RolName rolName;

   private String imgUser;


}
