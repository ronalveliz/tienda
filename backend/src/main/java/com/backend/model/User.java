package com.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "clientes")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String firtsName;

    @Column(nullable = false)
    private  String lastName;

    @Column(nullable = false, unique = true)
    private  String email;

    private String password;

    private String phone;

}
