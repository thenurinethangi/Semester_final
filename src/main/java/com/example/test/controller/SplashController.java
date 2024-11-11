package com.example.test.controller;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashController {

    @FXML
    private Label text;

    @FXML
    private Label text1;

    @FXML
    private Button tapToContinuebtn;

    @FXML
    void tapToContinueOnAction(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000));
            translateTransition.setFromX(0);
            translateTransition.setToX(-300);
            translateTransition.setCycleCount(1);
            translateTransition.setAutoReverse(true);

            // Set the transition to the current scene
            translateTransition.setNode(stage.getScene().getRoot());

            // Set the on finished event to switch to the new scene
            translateTransition.setOnFinished(e -> {
                stage.setScene(scene);
                stage.centerOnScreen();
            });

            // Play the transition
            translateTransition.play();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
