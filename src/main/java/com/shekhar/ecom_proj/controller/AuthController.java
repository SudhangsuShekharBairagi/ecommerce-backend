package com.shekhar.ecom_proj.controller;

import com.shekhar.ecom_proj.dto.LoginRequest;
import com.shekhar.ecom_proj.dto.LoginResponse;
import com.shekhar.ecom_proj.model.Users;
import com.shekhar.ecom_proj.security.JwtService;
import com.shekhar.ecom_proj.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user){
        try{
            Users registeredUser = userService.userRegistration(user);

            registeredUser.setPassword(null);

            return new ResponseEntity<>(
                    registeredUser,
                    HttpStatus.CREATED
            );
        }catch (RuntimeException e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

            try{
                Authentication authentication = authenticationManager.authenticate(token);
//        System.out.println(authentication);
                UserDetails userDetails =
                        (UserDetails) authentication.getPrincipal();

//        System.out.println(userDetails.getAuthorities());

                String jwtToken = jwtService.generateToken(userDetails);

//        System.out.println(jwtToken);

                LoginResponse response =
                        new LoginResponse(
                                jwtToken,
                                userDetails.getAuthorities()
                                        .iterator()
                                        .next()
                                        .getAuthority(),
                                userDetails.getUsername()
                        );


                return ResponseEntity.ok(response);

            } catch (Exception e) {
                return new ResponseEntity<>(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }
    }
}
