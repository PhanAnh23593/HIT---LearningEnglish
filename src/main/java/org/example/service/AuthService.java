    package org.example.service;

    import org.example.model.User;

    public interface AuthService {
        boolean Register(User user) throws IllegalArgumentException;
        User Login(String username, String password) throws IllegalArgumentException;
        boolean changePassword(String username, String oldPassword, String newPassword);

    }
