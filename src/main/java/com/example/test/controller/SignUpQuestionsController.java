package com.example.test.controller;

import com.example.test.dto.SignInQuestionsDto;
import com.example.test.dto.SignUpDto;
import com.example.test.model.SignUpQuestionsModel;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpQuestionsController {

    @FXML
    private TextField fquestion;

    @FXML
    private TextField squestion;

    @FXML
    private TextField tquestion;

    @FXML
    private Button next;

    @FXML
    private Button back;

    private FXMLLoader fxmlLoader;
    private Parent root;
    private Scene scene;
    private Stage stage;

    private SignUpDto dto;
    private Stage s1;
    private SignUpQuestionsModel signUpQuestionsModel;

    public SignUpQuestionsController(){

        try {
            signUpQuestionsModel = new SignUpQuestionsModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void backOnAction(ActionEvent event) throws IOException {

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void nextOnAction(ActionEvent event) throws IOException, SQLException {

        String fAnswer = fquestion.getText();
        String sAnswer = squestion.getText();
        String tAnswer = tquestion.getText();

        if(fAnswer.equals("") || sAnswer.equals("") || tAnswer.equals("")){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("Please answer all the questions");
            alert.setContentText("You should answer all the three questions before submit the answers");
            alert.showAndWait();
        }
        else{

            SignInQuestionsDto signInQuestionsDto = new SignInQuestionsDto(fAnswer,sAnswer,tAnswer,dto.getUserName());
            String response = signUpQuestionsModel.register(dto,signInQuestionsDto);

            if(response.equals("All Done")){

                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();

                fxmlLoader = new FXMLLoader(getClass().getResource("/view/Splash.fxml"));
                root = fxmlLoader.load();
                scene = new Scene(root);
                stage = s1;

                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000));
                translateTransition.setFromX(0);
                translateTransition.setToX(-300);
                translateTransition.setCycleCount(1);
                translateTransition.setAutoReverse(true);

                translateTransition.setNode(s1.getScene().getRoot());

                translateTransition.setOnFinished(e -> {
                    stage.setScene(scene);
                    stage.centerOnScreen();
                });

                translateTransition.play();

            }
            else{

                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Something went wrong");
                alert.setHeaderText("Please try again later");
                alert.setContentText(response);
                alert.showAndWait();

            }

        }


    }


    public void setSignUpData(SignUpDto signUpDto, Stage stage){

        dto = signUpDto;
        s1 = stage;

    }

}
