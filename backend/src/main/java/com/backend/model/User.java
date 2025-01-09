package com.backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String firstName;

    private  String lastName;

    private  String email;

    private String password;

    private String phone;

   @Enumerated(EnumType.STRING)
    private RolName rolName;

    private String imgUser;

}
