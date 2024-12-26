package com.backend.dto;

import com.backend.model.RolName;


public record NuevoUsuarioRegister(

        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        RolName roleName,
        String imgUser
        )
{
}
