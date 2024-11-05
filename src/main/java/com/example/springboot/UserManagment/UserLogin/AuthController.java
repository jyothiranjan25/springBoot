package com.example.springboot.UserManagment.UserLogin;

import com.example.springboot.UserManagment.AppUser.AppUserDTO;
import com.example.springboot.core.SpringSecurity.JwtUtil;
import com.example.springboot.core.SpringSecurity.TokenBlacklistUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/rest/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistUtil tokenBlacklistService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody AppUserDTO loginReq) {
        try {
            String userNameEmail;
            if (loginReq.getUsername() != null) {
                userNameEmail = loginReq.getUsername();
            } else {
                userNameEmail = loginReq.getEmail();
            }
            // Authenticate the user
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userNameEmail, loginReq.getPassword()));
            String email = authentication.getName();
            AppUserDTO user = new AppUserDTO();
            if (Objects.equals(email, loginReq.getEmail())) {
                user.setEmail(email);
            } else {
                user.setUsername(email);
            }

            // Create a token for the user
            String token = jwtUtil.createToken(user);
            user.setToken(token);
            return ResponseEntity.ok(user);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}