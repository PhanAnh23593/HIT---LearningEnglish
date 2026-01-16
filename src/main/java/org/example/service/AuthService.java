package org.example.service;

import org.example.model.User;

public interface AuthService {
    boolean Register(User user) throws IllegalArgumentException;
    User Login(String username, String password);
    boolean Logout(String username, String password);
    boolean changePassword(String username, String oldPassword, String newPassword);

}
