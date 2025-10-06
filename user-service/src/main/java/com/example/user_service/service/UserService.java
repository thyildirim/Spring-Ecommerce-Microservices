package com.example.user_service.service;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.ProfileDTO;
import com.example.user_service.entity.User;
import com.example.user_service.exception.HttpException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.JwtUtil;
import com.example.user_service.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public String register(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent())
        {
            throw new HttpException(CONFLICT,"Email already exists");
        }
        User requestUser = new User();
        requestUser.setName(user.getName());
        requestUser.setEmail(user.getEmail());
        requestUser.setPassword(passwordUtil.hashPassword(user.getPassword()));
        requestUser.setAge(user.getAge());
        requestUser.setRole(User.Role.CUSTOMER); // dışarıdan gelen role'ü dikkate alma
        userRepository.save(requestUser);
        return "You have successfully registered";
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new HttpException(UNAUTHORIZED, "Invalid credentials"));

        if (!passwordUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new HttpException(UNAUTHORIZED, "Invalid credentials");
        }

        user.setLastLoginTime(LocalDateTime.now());

        String token = jwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRole()));

        return new LoginResponse(token);
    }

    public ProfileDTO profile(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new HttpException(NOT_FOUND , "User not found"));
        return new ProfileDTO(user);
    }

}
