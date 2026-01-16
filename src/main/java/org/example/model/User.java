package org.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    int id;
    String username;
    String password;
    String fullName;
    String email;
    String phoneNumber;
    String avatar;
    String role;
    int status;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
    LocalDateTime lastLogin;
}