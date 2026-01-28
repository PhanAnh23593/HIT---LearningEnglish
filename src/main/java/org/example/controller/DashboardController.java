package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lblMajor;

    @FXML
    private Label lblUsername;


    @FXML
    void onEditProfile() {
    }

    @FXML
    void onLearnVocabulary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Learning/LearningVocab.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("LearningVocabulary");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogout() {

    }

    @FXML
    void onReview() {

    }

    @FXML
    void onSpeaking() {

    }

    @FXML
    void onText() {

    }

}
