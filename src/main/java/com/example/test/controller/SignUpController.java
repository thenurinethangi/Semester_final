package com.example.test.controller;

import com.example.test.dto.SignUpDto;
import com.example.test.logindata.LoginDetails;
import com.example.test.model.SignUpModel;
import com.example.test.validation.UserInputValidation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class SignUpController {

    @FXML
    private Pane pane1;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @FXML
    private Button signUpbtn1;

    @FXML
    private Button signInbtn1;

    @FXML
    private Button exitbtn;

    @FXML
    private TextField fullName;

    @FXML
    private TextField userName;

    private SignUpModel signUpModel;


    public SignUpController(){

        try {
            signUpModel = new SignUpModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void signInOnAction(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

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

    @FXML
    void signUpOnAction(ActionEvent event) throws IOException, SQLException {

        String uName = userName.getText();
        String name = fullName.getText();
        String em = email.getText();
        String pWord = password.getText();

        if(uName.isEmpty() || name.isEmpty() || em.isEmpty() || pWord.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("Please enter all the require details to sign up");
            alert.setContentText("You should enter the personal details to sign up process");
            alert.showAndWait();
        }
        else{

            boolean b1 = UserInputValidation.checkUserNameValidation(uName);
            boolean b2 = UserInputValidation.checkNameValidation(name);
            boolean b3 = UserInputValidation.checkEmailValidation(em);
            boolean b4 = UserInputValidation.checkPasswordValidation(pWord);

            if(b1 && b2 && b3 && b4){

                SignUpDto signUpDto = new SignUpDto(uName,name,em,pWord);
                String result = signUpModel.checkUserNameAlreadyExist(signUpDto);
                if(result.equals("this user name exist")){

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("This user name already exist");
                    alert.setContentText("This user name already exist, please sign in");
                    alert.showAndWait();

                    clear();
                }
                else{

                    LoginDetails loginDetails = LoginDetails.getInstance(uName,pWord);

                    Stage s1 = (Stage)((Node)event.getSource()).getScene().getWindow();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SignUpQuestions.fxml"));
                    Parent root = fxmlLoader.load();

                    SignUpQuestionsController signUpQuestionsController = fxmlLoader.getController();
                    signUpQuestionsController.setSignUpData(signUpDto,s1);

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setX(70);
                    stage.setY(55);
                    stage.show();

                }
            }
            else {
                String response1 ="";
                String response2 ="";
                String response3 ="";
                String response4 ="";

                if(!b1){
                   response1 = "Your user name not valid, user name should have at least 7 characters including special characters and numbers, ";
                }
                if(!b2){
                    response2 = "Your name not valid, please enter your full name, ";
                }
                if(!b3){
                    response3 = "Your email not valid, please enter valid email address, ";
                }
                if(!b4){
                    response4 = "Your password not valid, password should have at least 7 characters including special characters and numbers, ";
                }

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid details input");
                alert.setHeaderText("Input field data are incorrect double check before sign up");
                alert.setContentText(response1+response2+response3+response4);
                alert.showAndWait();

                clear();

            }

        }

    }

    public void exitOnAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Click OK to confirm, or Cancel to abort.");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){

            System.exit(0);
        }
        else{
            return;
        }
    }


    public void clear(){

        userName.setText("");
        fullName.setText("");
        email.setText("");
        password.setText("");
    }
}
