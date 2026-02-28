package org.example.controller.Profile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.constant.AppError;
import org.example.constant.AppMessage;
import org.example.constant.AppSuccsess;
import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class EditProfileController {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDay;

    private User currentUser = UserSession.currentUser;
    private String selectedImagePath = null;
    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(60, 60, 60);
        imgAvatar.setClip(clip);

        if (currentUser != null) {
            txtFullName.setText(currentUser.getFullName());
            txtPhone.setText(currentUser.getPhoneNumber());
            txtEmail.setText(currentUser.getEmail());
            if (currentUser.getBirthday() != null) {
                dpBirthDay.setValue(currentUser.getBirthday());
            }
            if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                try {
                    imgAvatar.setImage(new Image("file:" + currentUser.getAvatar()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void onChangeAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) txtFullName.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            imgAvatar.setImage(new Image("file:" + selectedImagePath));
        }
    }

    @FXML
    void onSave(ActionEvent event) {
        try {
            String newName = txtFullName.getText().trim();
            String newPhone = txtPhone.getText().trim();
            String newEmail = txtEmail.getText().trim();
            LocalDate newDob = dpBirthDay.getValue();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "ERROR", AppError.REGISTER_EMAIL_INVALID);
                return;
            }

            currentUser.setFullName(newName);
            currentUser.setPhoneNumber(newPhone);
            currentUser.setEmail(newEmail);
            currentUser.setBirthday(newDob);
            if (selectedImagePath != null) {
                currentUser.setAvatar(selectedImagePath);
            }

            userDAO.updateUserProfile(currentUser);

            showAlert(Alert.AlertType.INFORMATION, "Success", AppSuccsess.SUCCESS_INFORMATION_CHANGE);

            onCancel(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "ERROR", AppMessage.INFORMATION_EMPTY_WARNING);
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DASHBOARD");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onChangePassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Profile/ChangePassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtFullName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DASHBOARD");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}