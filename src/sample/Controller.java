package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

    @FXML
    public Button openFile;
    @FXML
    private Button backBtn;
    @FXML
    private Button forwardBtn;
    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Button playBtn, pauseBtn, resetBtn;
    @FXML
    private Slider volSlider;
    @FXML
    private ComboBox<String> speedBox;
//    @FXML
//    private ProgressBar songProgress;

    private MediaPlayer mediaPlayer;
    private String path;


    private Timer timer;
    private TimerTask task;
    private boolean running;


    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void openFileMethod(ActionEvent actionEvent) {

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }
        speedBox.setOnAction(this::changeSpeed);
        volSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volSlider.getValue() * 0.01);
            }
        });

        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a .mp4 file", ".mp4");
//        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);

        path = file.toURI().toString();

        if (path != null) {
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            int index = path.lastIndexOf("/");
            songLabel.setText(path.substring(index + 1));


            volSlider.setValue(mediaPlayer.getVolume() * 100);
            volSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volSlider.getValue() / 100);
                }
            });
        }
        playBtn.setDisable(false);
        pauseBtn.setDisable(false);
        resetBtn.setDisable(false);
    }



    public void changeSpeed(ActionEvent actionEvent) {
        if (speedBox.getValue() == null) {
            mediaPlayer.setRate(1);
        }
        else {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length()-1)) * 0.01);
        }
    }

    public void playMedia() {
        beginTimer();
        changeSpeed(null);
        playBtn.setDisable(true);
        pauseBtn.setDisable(false);
        mediaPlayer.setVolume(volSlider.getValue() * 0.01);
        mediaPlayer.play();

    }

    public void pauseMedia() {
        playBtn.setDisable(false);
        pauseBtn.setDisable(true);
        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia() {
        cancelTimer();
//        songProgress.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0.0));
    }

    public void backFive(ActionEvent actionEvent) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));

    }

    public void forwardFive(ActionEvent actionEvent) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
//                running = true;
//                double current = mediaPlayer.getCurrentTime().toSeconds();
//                double end = media.getDuration().toSeconds();
////                songProgress.setProgress(current/end);
//
//                if (current/end == 1) {
//                    cancelTimer();
//                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

}
