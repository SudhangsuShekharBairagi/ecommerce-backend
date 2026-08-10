package com.shekhar.ecom_proj.controller;

import com.shekhar.ecom_proj.dto.ProfileEditDto;
import com.shekhar.ecom_proj.dto.ProfileUserDTO;
import com.shekhar.ecom_proj.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ProfileUserDTO> getProfile(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(userService.getProfileDetails(email));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(Authentication authentication, @RequestBody ProfileEditDto profileEditDto){
        try {
            String email = authentication.getName();
            userService.editProfile(email, profileEditDto);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
            HttpStatus.BAD_REQUEST
                    );
        }

    }
    @PutMapping("/editImage")
    public ResponseEntity<?> updateImage(Authentication authentication, @RequestPart MultipartFile imageFile){
       try{
           userService.updateImage(authentication,imageFile);
           return ResponseEntity.ok("Update Successfull");
       } catch (Exception e) {
           return new ResponseEntity<>(
                   e.getMessage(),
                   HttpStatus.BAD_REQUEST
           );
       }
    }
}