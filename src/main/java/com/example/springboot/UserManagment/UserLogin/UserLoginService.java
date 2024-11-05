package com.example.springboot.UserManagment.UserLogin;

import com.example.springboot.UserManagment.AppUser.AppUser;
import com.example.springboot.UserManagment.AppUser.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        AppUser appUser = (AppUser) appUserRepository.findByEmailOrUsername(user,user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + user));
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return org.springframework.security.core.userdetails.User.builder()
                .username(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}
