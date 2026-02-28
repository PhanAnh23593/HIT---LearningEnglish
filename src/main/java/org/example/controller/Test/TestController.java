    package org.example.controller.Test;

    import javafx.animation.PauseTransition;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.ProgressBar;
    import javafx.scene.media.Media;
    import javafx.scene.media.MediaPlayer;
    import javafx.stage.Stage;
    import javafx.util.Duration;
    import org.example.dao.SaveUserDAO;
    import org.example.dao.VocabularyDAO;
    import org.example.model.User;
    import org.example.model.Vocabulary;
    import org.example.service.impl.GoogleTTSServiceImpl;
    import org.example.utils.UserSession;

    import java.io.File;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;
    import java.util.Random;

    public class TestController {

        @FXML private Button btnA, btnB, btnC, btnD, btnAudio;
        @FXML private Label lbQuestion, lbTitle;
        @FXML private ProgressBar lbprogress;
        @FXML private Label lbprogresstag;

        private final VocabularyDAO vocabularyDAO = new VocabularyDAO();
        private final SaveUserDAO saveUserDAO = new SaveUserDAO();
        private final GoogleTTSServiceImpl ggTTS = new GoogleTTSServiceImpl();

        private List<Vocabulary> testList;
        private int currentIndex = 0;
        private int score = 0;
        private String currentCorrectAnswer = "";
        private int currentQuizType = 0;
        private MediaPlayer media;
        private final Random random = new Random();

        @FXML
        public void initialize() {
            User currentUser = UserSession.currentUser;
            testList = vocabularyDAO.getAllVocabularyTest(currentUser.getId(), currentUser.getMajor());
            currentIndex = 0;
            score = 0;
            showQuestion(currentIndex);
        }

        public void showQuestion(int index) {
            try {
                blockButtons(false);
                resetButtonStyles();

                if (index >= testList.size()) {
                    finishTest();
                    return;
                }

                Vocabulary currentVocab = testList.get(index);
                lbprogress.setProgress((double) (index + 1) / testList.size());
                lbprogresstag.setText((index + 1) + "/" + testList.size());

                currentQuizType = random.nextInt(3);
                List<Vocabulary> wrongAnswers = new ArrayList<>(testList);
                wrongAnswers.remove(currentVocab);
                Collections.shuffle(wrongAnswers);

                List<String> answerTexts = new ArrayList<>();
                if (currentQuizType == 0) {
                    lbTitle.setText("Word");
                    lbQuestion.setText(currentVocab.getWord());
                    btnAudio.setVisible(true);
                    currentCorrectAnswer = currentVocab.getMeaning();
                    answerTexts.add(currentCorrectAnswer);
                    for (int i = 0; i < Math.min(3, wrongAnswers.size()); i++) {
                        answerTexts.add(wrongAnswers.get(i).getMeaning());
                    }

                } else if (currentQuizType == 1) {
                    lbTitle.setText("Audio");
                    lbQuestion.setText("Lắng nghe âm thanh và trả lời");
                    btnAudio.setVisible(true);
                    playAudio(currentVocab);

                    currentCorrectAnswer = currentVocab.getMeaning();
                    answerTexts.add(currentCorrectAnswer);
                    for (int i = 0; i < Math.min(3, wrongAnswers.size()); i++) {
                        answerTexts.add(wrongAnswers.get(i).getMeaning());
                    }

                } else {
                    lbTitle.setText("Sentence");
                    lbQuestion.setText(currentVocab.getExample());
                    btnAudio.setVisible(false);

                    currentCorrectAnswer = currentVocab.getExampleMeaning();
                    answerTexts.add(currentCorrectAnswer);
                    for (int i = 0; i < Math.min(3, wrongAnswers.size()); i++) {
                        answerTexts.add(wrongAnswers.get(i).getExampleMeaning());
                    }
                }



                Collections.shuffle(answerTexts);
                btnA.setText(answerTexts.size() > 0 ? answerTexts.get(0) : "");
                btnB.setText(answerTexts.size() > 1 ? answerTexts.get(1) : "");
                btnC.setText(answerTexts.size() > 2 ? answerTexts.get(2) : "");
                btnD.setText(answerTexts.size() > 3 ? answerTexts.get(3) : "");

                btnA.setVisible(answerTexts.size() > 0);
                btnB.setVisible(answerTexts.size() > 1);
                btnC.setVisible(answerTexts.size() > 2);
                btnD.setVisible(answerTexts.size() > 3);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void checkAnswer(Button btn) {
            try {
                blockButtons(true);
                String selectedAnswer = btn.getText();
                Vocabulary currentVocab = testList.get(currentIndex);
                int userId = UserSession.currentUser.getId();

                if (selectedAnswer.equals(currentCorrectAnswer)) {
                    btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    score++;
                    saveUserDAO.updateVocabularyStatus(userId, currentVocab.getId(), 3);
                } else {
                    btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    saveUserDAO.resetVocabulary(userId, currentVocab.getId());
                    showCorrectColor();
                }

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {
                    currentIndex++;
                    showQuestion(currentIndex);
                });
                pause.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showCorrectColor() {
            if (btnA.getText().equals(currentCorrectAnswer)) btnA.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (btnB.getText().equals(currentCorrectAnswer)) btnB.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (btnC.getText().equals(currentCorrectAnswer)) btnC.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            if (btnD.getText().equals(currentCorrectAnswer)) btnD.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        }

        private void finishTest() {
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kết quả");
                if(score>=10) {
                    alert.setHeaderText("Bạn giỏi rồi");
                    alert.setContentText("Điểm số của bạn: " + score + " / " + testList.size());
                    alert.showAndWait();
                    onBack();
                }else{
                    alert.setHeaderText("Bạn nên học thêm");
                    alert.setContentText("Điểm số của bạn: " + score + " / " + testList.size());
                    alert.showAndWait();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) btnA.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.centerOnScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @FXML void onbtnA() { checkAnswer(btnA); }
        @FXML void onbtnB() { checkAnswer(btnB); }
        @FXML void onbtnC() { checkAnswer(btnC); }
        @FXML void onbtnD() { checkAnswer(btnD); }

        @FXML
        void playAudioManual() {
            if (currentIndex < testList.size()) {
                playAudio(testList.get(currentIndex));
            }
        }

        private void playAudio(Vocabulary vocab) {
            String audioUrl = vocab.getAudio();
            if (audioUrl == null || audioUrl.isEmpty()) {
                audioUrl = ggTTS.getAudioPath(vocab.getWord());
            }
            if (audioUrl != null) {
                try {
                    if (media != null) { media.stop(); media.dispose(); }
                    if (!audioUrl.startsWith("http") && !audioUrl.startsWith("file:")) {
                        audioUrl = new File(audioUrl).toURI().toString();
                    }
                    media = new MediaPlayer(new Media(audioUrl));
                    media.play();
                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        @FXML
        void onBack() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard/DashBoard.fxml")); // Trở về Dashboard
                Parent root = loader.load();
                Stage stage = (Stage) btnA.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void blockButtons(boolean block) {
            btnA.setDisable(block); btnB.setDisable(block);
            btnC.setDisable(block); btnD.setDisable(block);
        }

        private void resetButtonStyles() {
            String def = "-fx-background-color: white; -fx-text-fill: black;";
            btnA.setStyle(def); btnB.setStyle(def);
            btnC.setStyle(def); btnD.setStyle(def);
        }
    }