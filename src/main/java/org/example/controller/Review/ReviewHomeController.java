package org.example.controller.Review;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.example.dao.VocabularyDAO;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.service.DailyProgressService;
import org.example.service.impl.DailyProgressServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;
import java.util.List;

public class ReviewHomeController {



    @FXML
    private Label alertProcess;

    @FXML
    private ProgressBar lbprocess;

    private final DailyProgressService progressService = new DailyProgressServiceImpl();

    @FXML
    public void initialize() {
        progressService.checkAndResetDailyProgress();
        updateProgressBar();
    }

    private void updateProgressBar() {

        boolean doneLearning = progressService.isLearningDone();
        boolean doneFlashcard = progressService.isFlashcardDone();
        boolean doneQuiz = progressService.isQuizDone();

        int totalGoals = 3;
        int completed = 0;
        if (doneLearning) completed++;
        if (doneFlashcard) completed++;
        if (doneQuiz) completed++;
        double prc = (double) completed / totalGoals;
        lbprocess.setProgress(prc);
        int percent = (int) (prc * 100);
        if (percent == 100) {
            alertProcess.setText("Finish(3/3)");
        } else {
            alertProcess.setText(percent + "% (" + completed + "/3)");
        }
    }

    @FXML
    void onAudioShow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/ReviewService/QuizAudioView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lbprocess.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ReViewHome");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onFlashcardshow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/ReviewService/FlashCardView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lbprocess.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FlashCardReview");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSentence() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/ReviewService/QuizSentenceView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lbprocess.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ReViewHome");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onWordShow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/ReviewService/QuizWordView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lbprocess.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FlashCardReview");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onshowList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/Reviewlist.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lbprocess.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("ReViewHome");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onExit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lbprocess.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DASHBOARD");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
