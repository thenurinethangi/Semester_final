package com.example.test.controller;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Label text;

    @FXML
    private Label precentage;

    @FXML
    private Button tapbtn;

    private FXMLLoader fxmlLoader;
    private Parent root;
    private Scene scene;
    private Stage stage;


    @FXML
    public void tapOnAction(ActionEvent event) throws IOException {

        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            root = fxmlLoader.load();
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000));
            translateTransition.setFromX(0);
            translateTransition.setToX(-300);
            translateTransition.setCycleCount(1);
            translateTransition.setAutoReverse(true);

            translateTransition.setNode(stage.getScene().getRoot());

            translateTransition.setOnFinished(e -> {
                stage.setScene(scene);
                stage.centerOnScreen();
            });

            translateTransition.play();

        }
        catch (IOException e){

            System.out.println(e.getMessage());

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tapbtn.setDisable(true);
        fill();

    }

    public void fill(){

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {

                for (int i = 1; i <= 100; i++) {
                    try {
                        Thread.sleep(65);
                    } catch (InterruptedException e) {

                        System.out.println(e.getMessage());
                    }

                    updateProgress(i, 100);
                    updateMessage(i + "%");
                    if(i==100){

                        tapbtn.setStyle("-fx-text-fill: #007AFF");
                        tapbtn.setDisable(false);

                    }
                }
                return null;
            }
        };

        progressbar.progressProperty().bind(task.progressProperty());
        precentage.textProperty().bind(task.messageProperty());


        new Thread(task).start();

    }

}


