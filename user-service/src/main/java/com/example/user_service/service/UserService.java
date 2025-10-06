package com.example.user_service.service;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.entity.User;
import com.example.user_service.exception.HttpException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;


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


}
