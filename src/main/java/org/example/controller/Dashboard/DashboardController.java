package org.example.controller.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.constant.AppError;
import org.example.constant.AppMessage;
import org.example.dao.VocabularyDAO;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.example.utils.UserSession.currentUser;

public class DashboardController {

    @FXML
    private Button btnReview;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lbmessage;

    @FXML
    private Label lblMajor;

    @FXML
    private Label lblUsername;

    @FXML
    private Button btnTest;

    private final VocabularyDAO vocabDAO = new VocabularyDAO();

    @FXML
    public void initialize(){
        try {
            User currentUser = UserSession.currentUser;
            if(currentUser == null ) { throw new Exception(AppError.LOAD_IDUSER_FAIL); }

            String avatarPath = currentUser.getAvatar();
            Image imageToLoad = null;

            if (avatarPath != null && !avatarPath.isEmpty()) {
                File file = new File(avatarPath);
                if (file.exists()) {
                    String localUrl = file.toURI().toString();
                    imageToLoad = new Image(localUrl);
                }
                else if (!file.isAbsolute()) {
                    try {
                        imageToLoad = new Image(getClass().getResourceAsStream("/images/" + avatarPath));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (imageToLoad == null) {
                imageToLoad = new Image(getClass().getResourceAsStream("/images/default.png"));
            }
            imgAvatar.setImage(imageToLoad);
            double radius = imgAvatar.getFitWidth() / 2;
            Circle clip = new Circle(radius, radius, radius);
            imgAvatar.setClip(clip);
            lblUsername.setText(currentUser.getFullName());
            lblMajor.setText("Chuyên ngành " + currentUser.getMajor());

            LocalDate today = java.time.LocalDate.now();
            LocalDate lastDate = currentUser.getLastLearningDate();
            User currentUserTest = UserSession.currentUser;

            if( currentUserTest.isFirstlogin()){
                currentUserTest.setFirstlogin(false);
                lbmessage.setText(AppMessage.ALERT_FIRSTLOGIN);
            }
            else if (lastDate != null && lastDate.equals(today)) {
                lbmessage.setText(AppMessage.ALERT_COMPLETE4);
            }
            else {
                lbmessage.setText(AppMessage.ALERT_NOT_COMPLETE4);
            }




            int masteredCount = vocabDAO.countTestVocabularies(currentUser.getId(), currentUser.getMajor());

            if (masteredCount < 50) {
                btnTest.setDisable(true);
                btnTest.setText("Kiểm tra (" + masteredCount + "/50)");
                btnTest.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: #7f8c8d;");
            } else {
                btnTest.setDisable(false);
                btnTest.setText("Kiểm tra");
                btnTest.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void onEditProfile() {
    }

    @FXML
    void onLearnVocabulary() {
        try {
            User currentUser = UserSession.currentUser;
            LocalDate today = java.time.LocalDate.now();
            LocalDate lastDate = currentUser.getLastLearningDate();

            if (lastDate != null && lastDate.equals(today)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(AppMessage.ALERT_COMPLETE1);
                alert.setContentText(AppMessage.ALERT_COMPLETE2);
                alert.showAndWait();
                return;
            }


            List<Vocabulary> newWords = vocabDAO.getNewVocabularies(currentUser.getId(), currentUser.getMajor());
            if (newWords == null || newWords.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không tìm thấy từ vựng mới nào cho chuyên ngành này!");
                alert.showAndWait();
                return;
            }

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
        try {
            currentUser = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Authentication/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("LOGIN");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onReview() {
        try {
            User currentUser = UserSession.currentUser;
            List<Vocabulary> list = vocabDAO.getAllVocabularyReview(currentUser.getId(),currentUser.getMajor());

            if (list.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(AppMessage.ALER_REVIEW_NOTDATA);
                alert.setContentText(AppMessage.ALERT_REVIEW_NOTSTART);
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/ReviewHome.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ReviewVocabulary");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSpeaking() {
    }

    @FXML
    void onTest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Test/Test.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Test");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}