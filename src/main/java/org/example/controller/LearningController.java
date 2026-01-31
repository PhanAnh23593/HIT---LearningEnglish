package org.example.controller;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.constant.AppError;
import org.example.constant.AppMessage;
import org.example.constant.AppSuccsess;
import org.example.dao.SaveUserDAO;
import org.example.dao.UserDAO;
import org.example.dao.VocabularyDAO;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.service.impl.GoogleTTSServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;
import java.security.spec.ECField;
import java.time.LocalDate;
import java.util.List;

public class LearningController {

    @FXML
    private VBox backCard;

    @FXML
    private Button btnAudio;

    @FXML
    private Button btnFlip;

    @FXML
    private Button btnpass;


    @FXML
    private Button btnnext;

    @FXML
    private StackPane cardContainer;

    @FXML
    private VBox frontCard;

    @FXML
    private Label lbExample;

    @FXML
    private Label lbExampleMeaning;

    @FXML
    private Label lbIpa;

    @FXML
    private Label lbMeaning;

    @FXML
    private Label lbWord;




    private final VocabularyDAO vocabdao = new VocabularyDAO();
    private final SaveUserDAO saveuserdao = new SaveUserDAO();
    private final GoogleTTSServiceImpl ggttservice = new GoogleTTSServiceImpl();
    private final UserDAO userdao = new UserDAO();
    private List<Vocabulary> listvocabtoday ;
    private int indextoday = 0;
    private boolean checkshow= true;
    private MediaPlayer media;


    @FXML
    public void initialize() {
        cardContainer.setDepthTest(javafx.scene.DepthTest.ENABLE);
        frontCard.setRotationAxis(Rotate.Y_AXIS);
        backCard.setRotationAxis(Rotate.Y_AXIS);
        resetCardToFront();

        try {
            User currentUser = UserSession.currentUser;
            if (currentUser == null) {
                onBackToDashboard();
                return;
            }
            LocalDate today = LocalDate.now();
            if (currentUser.getLastLearningDate() != null && currentUser.getLastLearningDate().equals(today)) {
                showAlertComplete(AppMessage.ALERT_COMPLETE3);
                onBackToDashboard();
                return;
            }
            int userId = currentUser.getId();
            String major = currentUser.getMajor();
            this.listvocabtoday = vocabdao.getNewVocabularies(userId, major);
            if (this.listvocabtoday == null || this.listvocabtoday.isEmpty()) {
                onBackToDashboard();
                return;
            }
            indextoday = 0;
            Showcard(indextoday);

        } catch (Exception e) {
            e.printStackTrace();
            showAlertComplete("Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void Showcard(int index) {
        try {
            if (listvocabtoday == null || index < 0 || index >= listvocabtoday.size()) return;

            Vocabulary vocabindex = listvocabtoday.get(index);

            lbWord.setText(vocabindex.getWord());
            lbIpa.setText(vocabindex.getIpa());
            lbMeaning.setText(vocabindex.getMeaning());
            lbExample.setText(vocabindex.getExample());
            lbExampleMeaning.setText(vocabindex.getExampleMeaning());
            resetCardToFront();

            btnpass.setDisable(index == 0);
            btnnext.setDisable(false);
            btnAudio.setDisable(false);
            btnFlip.setDisable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetCardToFront() {
        frontCard.setVisible(true);
        frontCard.setRotate(0);
        backCard.setVisible(false);
        backCard.setRotate(-90);
        checkshow = true;
    }


    @FXML
    void onBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backCard.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DASHBOARD");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onFlipCard() {
        Duration duration =Duration.millis(300);

        VBox displaycard = checkshow ? frontCard : backCard;
        VBox hiddencard = checkshow ? backCard : frontCard;


        RotateTransition ror = new RotateTransition(duration,displaycard);
        ror.setAxis(Rotate.Y_AXIS);
        ror.setFromAngle(0);
        ror.setToAngle(90);
        ror.setInterpolator(Interpolator.EASE_IN);


        ror.setOnFinished(e -> {
            displaycard.setVisible(false);
            hiddencard.setVisible(true);


            RotateTransition ror2 = new RotateTransition(duration,hiddencard);
            ror2.setAxis(Rotate.Y_AXIS);
            ror2.setFromAngle(-90);
            ror2.setToAngle(0);
            ror2.setInterpolator(Interpolator.EASE_OUT);
            ror2.play();
            checkshow= !checkshow;
        });
        ror.play();
    }

    @FXML
    void onNextCard() {
        if (listvocabtoday == null || listvocabtoday.isEmpty()) return;

        try {
            if (indextoday < listvocabtoday.size() - 1) {
                indextoday++;
                Showcard(indextoday);
            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Complete");
                alert.setHeaderText("Bạn đã học hết 10 từ vựng.");
                alert.setContentText("Bạn muốn LƯU KẾT QUẢ hay ÔN TẬP LẠI?");

                ButtonType btnSave = new ButtonType("Lưu & Hoàn thành", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
                ButtonType btnReview = new ButtonType("Chưa lưu (Học lại)", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(btnSave, btnReview);
                alert.showAndWait().ifPresent(type -> {
                    if (type == btnSave) {
                        if (UserSession.currentUser == null) return;
                        int userId = UserSession.currentUser.getId();
                        for (Vocabulary v : listvocabtoday) {
                            saveuserdao.SaveLearningUser(userId, v.getId());
                        }
                        userdao.updateLastLearningDate(userId);
                        UserSession.currentUser.setLastLearningDate(LocalDate.now());
                        showAlertComplete(AppSuccsess.COMPLETE_LEARNING);
                        onBackToDashboard();
                    } else {
                        indextoday = 0;
                        Showcard(indextoday);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onPassCard() {
        if(indextoday > 0){
            indextoday--;
            Showcard(indextoday);
        }
    }

    @FXML
    void playAudio() {

    }

    private void showAlertComplete(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        btnAudio.setDisable(true);
        btnFlip.setDisable(true);
        btnnext.setDisable(true);
        btnpass.setDisable(true);
    }

}
