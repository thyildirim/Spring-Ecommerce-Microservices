package com.example.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    @Size(min = 3,max = 50,message = "Enter a valid name")
    private String name;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "Email is required")
    @Size(max = 100,message = "Email too long")
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 8,max = 100,message = "Enter a valid password ")
    private String password;

    @Column(nullable = false)
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 120, message = "Age cannot be greater than 120")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @Column(updatable = false,nullable = false)
    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private LocalDateTime lastLoginTime;

    public enum Role{
        CUSTOMER,
        ADMIN,
        SELLER
    }

    @PrePersist
    public void onCreated(){
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }


    @PreUpdate
    public void onUpdated(){
        updatedTime = LocalDateTime.now();
    }


}
