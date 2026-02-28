package org.example.controller.Profile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.constant.AppMessage;
import org.example.constant.AppSuccsess;
import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.impl.AuthServiceImpl;
import org.example.utils.UserSession;

public class ChangePasswordController {

    @FXML private PasswordField txtOldPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;

    private User currentUser = UserSession.currentUser;

    private AuthService authService = new AuthServiceImpl();

    @FXML
    void onSave(ActionEvent event) {
        String oldPwd = txtOldPassword.getText();
        String newPwd = txtNewPassword.getText();
        String confirmPwd = txtConfirmPassword.getText();

        if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "WARNING", AppMessage.INFORMATION_EMPTY_WARNING);
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Mật khẩu không khớp!");
            return;
        }

        try {
                authService.changePassword(currentUser.getUsername(), oldPwd, newPwd);
                showAlert(Alert.AlertType.INFORMATION, "Success", AppSuccsess.SUCCSESS_CHANGE_PASSWORD);
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml"));
                javafx.scene.Parent root = loader.load();
                Stage popupStage = (Stage) txtOldPassword.getScene().getWindow();
                popupStage.close();
                for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                    if (window instanceof Stage && window != popupStage) {
                        Stage mainStage = (Stage) window;
                        mainStage.setScene(new javafx.scene.Scene(root));
                        mainStage.setTitle("DASHBOARD");
                        mainStage.centerOnScreen();
                        break;
                    }
                }

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "WARNING", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        ((Stage) txtOldPassword.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}