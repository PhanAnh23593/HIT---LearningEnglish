package org.example.service.impl;

import org.example.constant.AppError;
import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.utils.UserSession;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {
    private UserDAO userDAO = new UserDAO();

    @Override
    public boolean Register(User user) throws IllegalArgumentException {


        //FULLNAME
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException(AppError.REGISTER_FULLNAME_EMPTY);
        }

        //USERNAME
        if (user.getUsername() == null || user.getUsername().length() < 5) {
            throw new IllegalArgumentException(AppError.REGISTER_USERNAME_SHORT);
        }
        if (!user.getUsername().matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException(AppError.REGISTER_USERNAME_INVALID_CHARS);
        }

        //PASSWORD
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException(AppError.REGISTER_PASSWORD_SHORT);
        }
        if (!user.getPassword().matches(".*[A-Z].*")) {
            throw new IllegalArgumentException(AppError.REGISTER_PASSWORD_NO_UPPER);
        }
        if (!user.getPassword().matches(".*[0-9].*")) {
            throw new IllegalArgumentException(AppError.REGISTER_PASSWORD_NO_DIGIT);
        }

        //EMAIL
        if (user.getEmail() == null || !user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException(AppError.REGISTER_EMAIL_INVALID);
        }

        //PHONE
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("^0\\d{9}$")) {
            throw new IllegalArgumentException(AppError.REGISTER_PHONE_INVALID);
        }

        //EXISTER
        if (userDAO.checkUsername(user.getUsername())) {
            throw new IllegalArgumentException(AppError.REGISTER_USERNAME_EXISTS);
        }
        if (userDAO.checkEmail(user.getEmail())) {
            throw new IllegalArgumentException(AppError.REGISTER_EMAIL_EXISTS);
        }

        return userDAO.register(user);
    }



    public User Login(String username, String password) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(AppError.LOGIN_INFO_EMPTY);
        }
        User user = userDAO.login(username, password);
        if (user == null) {
            throw new IllegalArgumentException(AppError.LOGIN_FAIL);
        }
        return user;
    }


    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (UserSession.currentUser == null) {
            throw new IllegalArgumentException(AppError.LOGIN_REQUIRED);
        }

        if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException(AppError.CHANGE_PASS_EMPTY);
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException(AppError.CHANGE_PASS_DUPLICATE);
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException(AppError.REGISTER_PASSWORD_SHORT);
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException(AppError.REGISTER_PASSWORD_NO_UPPER);
        }
        if (!newPassword.matches(".*[0-9].*")) {
            throw new IllegalArgumentException(AppError.REGISTER_PASSWORD_NO_DIGIT);
        }

        User currentUser = UserSession.currentUser;

        if (!BCrypt.checkpw(oldPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException(AppError.CHANGE_PASS_OLD_WRONG);
        }

        String newHashedPass = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        boolean isSuccess = userDAO.changePassword(currentUser.getId(), newHashedPass);

        if (isSuccess) {
            UserSession.currentUser.setPassword(newHashedPass);
        }

        return isSuccess;
    }



}
