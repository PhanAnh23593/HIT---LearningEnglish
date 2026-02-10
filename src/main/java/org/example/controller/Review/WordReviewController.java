package org.example.controller.Review;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.constant.AppMessage;
import org.example.constant.AppSuccsess;
import org.example.dao.SaveUserDAO;
import org.example.dao.VocabularyDAO;
import org.example.model.SaveUser;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.service.impl.DailyProgressServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WordReviewController {

    @FXML
    private Button btnA;

    @FXML
    private Button btnB;

    @FXML
    private Button btnC;

    @FXML
    private Button btnD;

    @FXML
    private Label lbWord;

    @FXML
    private ProgressBar lbprogress;

    @FXML
    private Label lbprogresstag;


    private final VocabularyDAO vocabularyDAO = new VocabularyDAO();
    private List<Vocabulary> reviewList;
    private int indexreview = 0;
    private  DailyProgressServiceImpl CheckQuiz = new DailyProgressServiceImpl();
    private final SaveUserDAO saveUserDAO = new SaveUserDAO();
    private User currentUser = UserSession.currentUser;


    public void initialize(){
        reviewList = vocabularyDAO.getAllVocabularyReview(currentUser.getId(),currentUser.getMajor());
        Collections.shuffle(reviewList);
        indexreview = 0;
        Showindex(indexreview);
    }

    public void Showindex(int index){
        try {
            BlockButton(false);
            btnA.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            btnB.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            btnC.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            btnD.setStyle("-fx-background-color: white; -fx-text-fill: black;");



            if (index >= reviewList.size()) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(AppSuccsess.REVIEW_GOOD);
                alert.setContentText(AppSuccsess.REVIEW_QUIZ_COMPLETE);
                alert.showAndWait();
                CheckQuiz.markQuizDone();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/ReviewHome.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnA.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("HomeReview");
                stage.centerOnScreen();
                return;
            }

            Vocabulary vocabulary = reviewList.get(index);
            lbWord.setText(vocabulary.getWord());
            lbprogress.setProgress((double) (indexreview+1) / reviewList.size());
            lbprogresstag.setText((indexreview + 1) + "/" + reviewList.size());

            List<Vocabulary> Wrong = new ArrayList<>(reviewList);
            Wrong.remove(vocabulary);
            Collections.shuffle(Wrong);

            List<Vocabulary> btnanswer = new ArrayList<Vocabulary>();
            btnanswer.add(vocabulary);
            if (Wrong.size() >= 3) {
                btnanswer.add(Wrong.get(0));
                btnanswer.add(Wrong.get(1));
                btnanswer.add(Wrong.get(2));
            } else {
                btnanswer.addAll(Wrong);
            }
            Collections.shuffle(btnanswer);

            btnA.setText(btnanswer.get(0).getMeaning());
            btnB.setText(btnanswer.get(1).getMeaning());
            btnC.setText(btnanswer.get(2).getMeaning());
            btnD.setText(btnanswer.get(3).getMeaning());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showColorCorrect(){
        try {
            String answercorrect = reviewList.get(indexreview).getMeaning();
            if (answercorrect.equals(btnA.getText())) btnA.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            else if (answercorrect.equals(btnB.getText())) btnB.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            else if (answercorrect.equals(btnC.getText())) btnC.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            else btnD.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void CheckAnswer(Button btn){
        try {
            BlockButton(true);
            String answer = btn.getText();
            if (answer.equals(reviewList.get(indexreview).getMeaning())) {
                btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                saveUserDAO.updateProgress(currentUser.getId(),reviewList.get(indexreview).getId(),true);
            } else {
                btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                saveUserDAO.updateProgress(currentUser.getId(),reviewList.get(indexreview).getId(),false);
                showColorCorrect();
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                indexreview++;
                Showindex(indexreview);
            });
            pause.play();

        } catch (Exception e){
            e.printStackTrace();
        }
    }



    @FXML
    void onbtnA() {
        CheckAnswer(btnA);
    }

    @FXML
    void onbtnB() {
        CheckAnswer(btnB);
    }

    @FXML
    void onbtnC() {
        CheckAnswer(btnC);
    }

    @FXML
    void onbtnD() {
        CheckAnswer(btnD);
    }


    @FXML
    void onBack(){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/ReviewHome.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnA.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("HomeReview");
            stage.centerOnScreen();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void BlockButton(boolean check) {
        btnA.setDisable(check);
        btnB.setDisable(check);
        btnC.setDisable(check);
        btnD.setDisable(check);
    }

}
