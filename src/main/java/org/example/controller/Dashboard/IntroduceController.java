package org.example.controller.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.dao.UserDAO;
import org.example.dao.VocabularyDAO;
import org.example.utils.UserSession;

import java.io.File;
import java.time.LocalDate;

public class IntroduceController {

    @FXML
    private ImageView Avatar;

    @FXML
    private DatePicker BirtDayUser;

    @FXML
    private Label FullnameUser;

    @FXML
    private ComboBox<String> getTag;



    private final VocabularyDAO vocabDao = new VocabularyDAO();
    private final UserDAO userDao = new UserDAO();


    private String SelectAvartar = "first_avatar.png";



    @FXML
    public  void initialize() {
        if(UserSession.currentUser!= null){
            String fullname = UserSession.currentUser.getFullName();
            if(fullname == null || fullname.isEmpty()){
                fullname = UserSession.currentUser.getUsername();
            }
            FullnameUser.setText(fullname);
        }

        try {
            getTag.getItems().addAll(vocabDao.getAllTags());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void ChooseAvata() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Avatar");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ImageFiles","*.jpg","*.jpeg","*.png"));
        File selectFile = fc.showOpenDialog(FullnameUser.getScene().getWindow());


        if (selectFile != null) {
            Image img = new Image(selectFile.toURI().toString());
            Avatar.setImage(img);
            SelectAvartar = selectFile.getAbsolutePath();
            double width = Avatar.getFitWidth();
            double height = Avatar.getFitHeight();
            double diameter = Math.min(width, height);
            double radius = diameter / 2;
            Circle clip = new Circle(radius, radius, radius);
            Avatar.setClip(clip);
        }

    }

    @FXML
    void StartApp() {
        int userId = UserSession.currentUser.getId();
        LocalDate birthDay = BirtDayUser.getValue();
        String selectedMajor = getTag.getValue();

        if (selectedMajor == null || selectedMajor.isEmpty()) {
            return;
        }

        if (userDao.FirstLogin(userId, SelectAvartar, birthDay, selectedMajor)) {
            UserSession.currentUser.setAvatar(SelectAvartar);
            UserSession.currentUser.setBirthday(birthDay);
            UserSession.currentUser.setMajor(selectedMajor);


            switchDashBoard(selectedMajor);

        } else {
            showAlert("ERROR", "");
        }
    }



    private void switchDashBoard(String tag) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBoard/DashBoard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) FullnameUser.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DashBoard");
            stage.centerOnScreen();

        }catch(Exception e){
            e.printStackTrace();
        }
    }




    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
