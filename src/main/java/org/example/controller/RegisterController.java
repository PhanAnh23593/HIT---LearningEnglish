package org.example.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.constant.AppError;
import org.example.constant.AppSuccsess;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.impl.AuthServiceImpl;

import java.io.IOException;

public class RegisterController {


    @FXML
    private PasswordField RgtConfirmPassword;

    @FXML
    private TextField RgtEmail;

    @FXML
    private TextField RgtFullName;

    @FXML
    private PasswordField RgtPassword;

    @FXML
    private TextField RgtPhone;

    @FXML
    private TextField RgtUsername;

    @FXML
    private Button btnRegister;

    @FXML
    private ProgressIndicator loadingSpinner;

    private final AuthService authService = new AuthServiceImpl();

    @FXML
    public void onRegisterClick() {
        String fullName = RgtFullName.getText();
        String username = RgtUsername.getText();
        String email = RgtEmail.getText();
        String phone = RgtPhone.getText();
        String password = RgtPassword.getText();
        String confirmPass = RgtConfirmPassword.getText();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorAlert(AppError.CHANGE_PASS_EMPTY);
            return;
        }


        if (!password.equals(confirmPass)) {
            showErrorAlert(AppError.REGISTER_PASS_CONFIRM_WRONG);
            return;
        }
        loadingSpinner.setVisible(true);
        btnRegister.setDisable(true);
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phone);
        newUser.setPassword(password);
        newUser.setRole("USER");
        newUser.setStatus(1);

        Task<Boolean> registerTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    Thread.sleep(500);
                    return authService.Register(newUser);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw e;
                }
            }
        };


        registerTask.setOnSucceeded(event -> {
            loadingSpinner.setVisible(false);
            btnRegister.setDisable(false);

            boolean isRegistered = registerTask.getValue();
            if (isRegistered) {
                showSuccessAlert(AppSuccsess.REGISTER_SUCCESS);
                switchToLogin();
            } else {
                showErrorAlert(AppError.REGISTER_FAIL);
            }
        });


        registerTask.setOnFailed(event -> {
            loadingSpinner.setVisible(false);
            btnRegister.setDisable(false);

            Throwable error = registerTask.getException();
            String errorMsg = error.getMessage() != null ? error.getMessage() : AppError.UNKNOWN_ERROR;
            showErrorAlert(errorMsg);
        });


        new Thread(registerTask).start();
    }

    @FXML
    public void onLoginLinkClick() {
        switchToLogin();
    }

    private void switchToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Authentication/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) RgtUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("LOGIN");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}