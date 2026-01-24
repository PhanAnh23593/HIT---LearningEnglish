package org.example.controller;

import javafx.animation.PauseTransition; // Import để làm trễ chuyển trang
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration; // Import thời gian
import org.example.constant.AppError;
import org.example.constant.AppSuccsess;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.impl.AuthServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;

public class LoginController {

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private TextField LoginUsername;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblMessage;

    @FXML
    private ProgressIndicator loadingSpinner;

    private final AuthService authService = new AuthServiceImpl();

    @FXML
    public void onLoginClick() {
        String username = LoginUsername.getText();
        String password = LoginPassword.getText();


        lblMessage.setText("");
        loadingSpinner.setVisible(true);
        btnLogin.setDisable(true);


        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                try {
                    Thread.sleep(500);
                    return authService.Login(username, password);
                } catch (Exception e) {
                    throw e;
                }
            }
        };


        loginTask.setOnSucceeded(event -> {
            loadingSpinner.setVisible(false);

            User user = loginTask.getValue();
            UserSession.currentUser = user;


            lblMessage.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            lblMessage.setText(AppSuccsess.LOGIN_SUCCESS);

            // Tạo 0.5s trễ
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(e -> switchToDashboard());
            pause.play();
        });


        loginTask.setOnFailed(event -> {
            loadingSpinner.setVisible(false);
            btnLogin.setDisable(false);

            Throwable error = loginTask.getException();
            String errorMsg = error.getMessage() != null ? error.getMessage() : AppError.LOGIN_FAIL;
            showErrorAlert(errorMsg);
        });

        new Thread(loginTask).start();
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR LOGIN");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onRegisterClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Authentication/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) LoginUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ONREGISTER");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DASHBOARD");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}