package org.example.controller.Review;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.constant.AppSuccsess;
import org.example.dao.VocabularyDAO;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.service.impl.DailyProgressServiceImpl;
import org.example.service.impl.GoogleTTSServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;
import java.util.List;

public class FlashCardReviewController {

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
    private final GoogleTTSServiceImpl ggttservice = new GoogleTTSServiceImpl();
    private List<Vocabulary> listvocabreview ;
    private int indexreview = 0;
    private boolean checkshow= true;
    private MediaPlayer media;
    private final DailyProgressServiceImpl checkReviewToDay = new DailyProgressServiceImpl();


    @FXML
    public void initialize() {
        cardContainer.setDepthTest(javafx.scene.DepthTest.ENABLE);
        frontCard.setRotationAxis(Rotate.Y_AXIS);
        backCard.setRotationAxis(Rotate.Y_AXIS);
        resetCardToFront();

        try {
            User currentUser = UserSession.currentUser;
            int userId = currentUser.getId();
            String major = currentUser.getMajor();
            listvocabreview = vocabdao.getAllVocabularyReview(userId, major);
            indexreview = 0;
            Showcard(indexreview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Showcard(int index) {
        try {
            Vocabulary vocabindex = listvocabreview.get(index);
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
    void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/ReviewHome.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backCard.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("HomeReview");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @FXML
    void onFlip() {
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
    void onNext() {
        try {
            if (indexreview < listvocabreview.size() - 1) {
                indexreview++;
                Showcard(indexreview);
            } else {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("complete");
                alert.setHeaderText(null);
                alert.setContentText(AppSuccsess.ALERT_REVIEW_FLASHCARD_COMPLETE);
                alert.showAndWait();
                checkReviewToDay.markFlashcardDone();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/ReviewHome.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backCard.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("HomeReview");
                stage.centerOnScreen();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onPass() {
        if(indexreview > 0){
            indexreview--;
            Showcard(indexreview);
        }
    }

    @FXML
    void playAudio() {
        if (listvocabreview == null || listvocabreview.isEmpty()) return;

        Vocabulary currentVocab = listvocabreview.get(indexreview);
        String audioUrl = currentVocab.getAudio();
        if (audioUrl == null || audioUrl.isEmpty()) {
            audioUrl = ggttservice.getAudioPath(currentVocab.getWord());
        }
        if (audioUrl != null) {
            try {
                if (media != null) { media.stop(); media.dispose(); }
                if (!audioUrl.startsWith("http") && !audioUrl.startsWith("file:")) {
                    java.io.File file = new java.io.File(audioUrl);
                    audioUrl = file.toURI().toString();
                }
                media = new MediaPlayer(new Media(audioUrl));
                media.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
