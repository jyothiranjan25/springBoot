package com.example.springboot.UserManagment.AppUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO {
    private Long id;
    private String username;
    private String password;
    private String token;
    private String email;
    private AppUserEnum userType;
    private Boolean isActive;
}
