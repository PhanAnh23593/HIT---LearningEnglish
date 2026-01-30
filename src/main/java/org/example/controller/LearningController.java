package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.example.constant.AppError;
import org.example.dao.SaveUserDAO;
import org.example.dao.VocabularyDAO;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.service.impl.GoogleTTSServiceImpl;
import org.example.utils.UserSession;

import java.io.IOException;
import java.security.spec.ECField;
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

    private List<Vocabulary> listvocabtoday ;
    private int indextoday = 0;
    private boolean checkshow= true;
    private MediaPlayer media;


    @FXML
    public void initialize(){
        frontCard.setVisible(true);
        frontCard.setRotate(0);



        backCard.setRotate(-90);
        backCard.setVisible(false);

        try {
            User currentUser = UserSession.currentUser;
            if(currentUser == null ) { throw new Exception(AppError.LOAD_IDUSER_FAIL); }

            int userId = currentUser.getId();
            String major = currentUser.getMajor();


            this.listvocabtoday = vocabdao.getNewVocabularies(userId,major);
            if(this.listvocabtoday.isEmpty() || this.listvocabtoday == null ){ throw new Exception(AppError.LOAD_VOCABUSER_FAIL); }
            indextoday = 0;
            Showcard(indextoday);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void Showcard(int index){
        try{
            if(index < 0 || index >= listvocabtoday.size() ){ throw new Exception(AppError.INDEX_INVALLID);}

            Vocabulary vocabindex = listvocabtoday.get(index);

            lbWord.setText(vocabindex.getWord());
            lbIpa.setText(vocabindex.getIpa());
            lbMeaning.setText(vocabindex.getMeaning());

            lbExample.setText(vocabindex.getExample());
            lbExampleMeaning.setText(vocabindex.getExampleMeaning());


            if(!checkshow){
                frontCard.setVisible(true);
                frontCard.setRotate(0);
                backCard.setVisible(false);
                backCard.setRotate(-90);
                checkshow = true;
            }



            btnpass.setDisable(index == 0);
            btnnext.setDisable(false);
            btnAudio.setDisable(false);


        }catch(Exception e){}

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

    }

    @FXML
    void onNextCard() {

    }

    @FXML
    void onPassCard() {

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
