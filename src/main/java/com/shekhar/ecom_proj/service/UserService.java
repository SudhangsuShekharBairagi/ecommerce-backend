package com.shekhar.ecom_proj.service;

import com.shekhar.ecom_proj.dto.ProfileEditDto;
import com.shekhar.ecom_proj.dto.ProfileUserDTO;
import com.shekhar.ecom_proj.model.UserAddress;
import com.shekhar.ecom_proj.model.Users;
import com.shekhar.ecom_proj.repo.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {
    private final UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }

    public Users userRegistration(Users user, MultipartFile imageFile) throws IOException {
        if(usersRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email Already registrar");
        }
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        if( imageFile != null && !imageFile.isEmpty()){
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            user.setImageUrl(imageUrl);
        }

        return usersRepository.save(user);
    }

    public ProfileUserDTO getProfileDetails(String email) {

       Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Something went wrong"));
        UserAddress address = user.getUserAddress();

        return ProfileUserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .city(address != null ? address.getCity() : null)
                .state(address != null ? address.getState() : null)
                .street(address != null ? address.getStreet() : null)
                .pinCode(address != null ? address.getPinCode() : null)
                .imageUrl(user.getImageUrl())
                .build();
    }

    public Users editProfile(String email, ProfileEditDto profileEditDto) {

        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Something went wrong"));

        UserAddress address = user.getUserAddress();

        user.setUsername(profileEditDto.getUsername());
        address.setCity(profileEditDto.getCity());
        address.setState(profileEditDto.getState());
        address.setPinCode(profileEditDto.getPinCode());
        address.setStreet(profileEditDto.getStreet());

        return  usersRepository.save(user);
    }

    public void updateImage(Authentication authentication, MultipartFile imageFile) throws IOException {
        Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("Something went wrong"));
        if( imageFile != null && !imageFile.isEmpty()){
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            user.setImageUrl(imageUrl);
        }

        usersRepository.save(user);
    }
}
