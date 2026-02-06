package org.example.controller.Review;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.dao.VocabularyDAO;
import org.example.model.User;
import org.example.model.Vocabulary;
import org.example.utils.UserSession;

import java.util.List;

public class ShowListReniewController {

    @FXML private Button onFindKey;
    @FXML private TextField txtKey;

    @FXML private TableView<Vocabulary> tableVocab;
    @FXML private TableColumn<Vocabulary, String> tbWord;
    @FXML private TableColumn<Vocabulary, String> tbMeaning;
    @FXML private TableColumn<Vocabulary, Void> tbStt;
    @FXML private TableColumn<Vocabulary, String> tbCountCorrect;

    private final VocabularyDAO vocabDAO = new VocabularyDAO();
    private ObservableList<Vocabulary> listShow;

    @FXML
    public void initialize() {
        tbStt.setCellFactory(col -> new TableCell<Vocabulary, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        tbWord.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWord()));
        tbMeaning.setCellValueFactory(new PropertyValueFactory<>("meaning"));
        tbCountCorrect.setCellValueFactory(cellData -> {
            int score = cellData.getValue().getCountCorrect();
            return new SimpleStringProperty(score + "/3");
        });
        loadData();
        onFindKey.setOnAction(e -> searchVocabulary());
    }

    private void loadData() {
        User currentUser = UserSession.currentUser;
            List<Vocabulary> vocabList = vocabDAO.getAllVocabularyReview(currentUser.getId(), currentUser.getMajor());
            listShow = FXCollections.observableArrayList(vocabList);
            tableVocab.setItems(listShow);
    }

    private void searchVocabulary() {
        String keyword = txtKey.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tableVocab.setItems(listShow);
        } else {
            ObservableList<Vocabulary> find = FXCollections.observableArrayList();
            for (Vocabulary v : listShow) {
                if (v.getWord().toLowerCase().contains(keyword) || v.getMeaning().toLowerCase().contains(keyword)) {
                    find.add(v);
                }
            }
            tableVocab.setItems(find);
        }
    }

    @FXML
    void onExits(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Review/HomeReview/ReviewHome.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtKey.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}