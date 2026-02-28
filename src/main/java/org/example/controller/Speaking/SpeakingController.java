package org.example.controller.Speaking;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.dao.VocabularyDAO;
import org.example.model.Vocabulary;
import org.example.service.impl.GoogleTTSServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SpeakingController {
    @FXML
    private Label lblWord;

    @FXML
    private Label lblPhonetic;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblRemaining;

    @FXML
    private Button btnMic;

    private final VocabularyDAO vocabularyDAO = new VocabularyDAO();
    private List<Vocabulary> speakingList;
    private int indexSpeaking = 0;
    private int audioPlayCount = 0;
    private final int MAX_PLAYS = 3;
    private final GoogleTTSServiceImpl ggTTservice = new GoogleTTSServiceImpl();
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        speakingList = vocabularyDAO.getAllVocabularySavebyTag(UserSession.currentUser.getId(), UserSession.currentUser.getMajor());
        Collections.shuffle(speakingList);
        indexSpeaking = 0;
        showWord(indexSpeaking);
    }

    public void showWord(int index) {
        if (speakingList == null || index >= speakingList.size()) {
            return;
        }

        Vocabulary current = speakingList.get(index);
        audioPlayCount = 0;

        if(lblRemaining != null) lblRemaining.setText("Lượt nghe: " + MAX_PLAYS);
        if(lblWord != null) lblWord.setText(current.getWord());
        if(lblPhonetic != null) lblPhonetic.setText("/" + current.getWord().toLowerCase() + "/");
        if(lblStatus != null) lblStatus.setText(""); // Xóa trạng thái cũ
    }

    @FXML
    void playAudio() {
        if (speakingList == null || speakingList.isEmpty() || audioPlayCount >= MAX_PLAYS) return;

        Vocabulary currentVocab = speakingList.get(indexSpeaking);
        String audioUrl = ggTTservice.getAudioPath(currentVocab.getWord());

        if (audioUrl != null) {
            try {
                if (mediaPlayer != null) { mediaPlayer.stop(); mediaPlayer.dispose(); }
                mediaPlayer = new MediaPlayer(new Media(audioUrl));
                mediaPlayer.play();

                audioPlayCount++;
                if(lblRemaining != null) lblRemaining.setText("Nói to lên bạn"+(MAX_PLAYS - audioPlayCount));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onRecord() {
        if (btnMic != null) btnMic.setDisable(true);
        if (lblStatus != null) lblStatus.setText("🎙️ Đang nghe");
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(event -> {
            if (btnMic != null) btnMic.setDisable(false);
        });
        pause.play();
    }

    @FXML
    void onNextWord() {
        try {
            indexSpeaking++;
            if (indexSpeaking < speakingList.size()) {
                showWord(indexSpeaking);
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Tuyệt vời!");
                    alert.setContentText("Complete");
                    alert.showAndWait();
                    onBack();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblWord.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}