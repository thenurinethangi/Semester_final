package com.example.test.controller;

import com.example.test.dto.SignInDto;
import com.example.test.dto.SignInQuestionsDto;
import com.example.test.logindata.LoginDetails;
import com.example.test.model.SignInModel;
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
import java.util.Optional;

public class SignInController {

    public Button forgotbtn;
    @FXML
    private Pane pane1;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Button signUpbtn;

    @FXML
    private Button signInbtn;

    @FXML
    private Button exitbtn;

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private SignInModel signInModel;
    private SignInQuestionsDto dto;


    public SignInController(){

        try {
            signInModel = new SignInModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @FXML
    void signUpOnAction(ActionEvent event) throws IOException {

        fxmlLoader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

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



    public void forgotPasswordOnAction(ActionEvent event) {

        String uName = userName.getText();

        if(uName.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("Please enter data to require field");
            alert.setContentText("You should enter your User Name to get back your password");
            alert.showAndWait();
        }

        else {

            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/ForgotPasswordQuestions.fxml"));
                root = fxmlLoader.load();

                ForgotPasswordQuestionsController forgotPasswordQuestionsController = fxmlLoader.getController();
                forgotPasswordQuestionsController.setUserName(uName);
                scene = new Scene(root);
                stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.setX(70);
                stage.setY(55);
                stage.show();


            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }


    public void signInOnAction(ActionEvent event) throws IOException {

        String uName = userName.getText();
        String pWord = password.getText();

        if(uName.equals("") || pWord.equals("")){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("Please fill in all required fields");
            alert.setContentText("You have not entered all the required information. Please fill in all the necessary fields before submitting.");
            alert.showAndWait();
        }
        else{

            SignInDto signInDto = new SignInDto(uName,pWord);

            try {
                String response = signInModel.signInAuthentication(signInDto);
                if(response.equals("All Correct")){

                    LoginDetails loginDetails = LoginDetails.getInstance(uName,pWord);
                    System.out.println(LoginDetails.getUserName());

                    fxmlLoader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
                    root = fxmlLoader.load();
                    scene = new Scene(root);
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();

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
                else{

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Double check user name and password");
                    alert.setContentText(response);
                    alert.showAndWait();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        clear();

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
        password.setText("");
    }


    public void setData(SignInDto signInDto){

        userName.setText(signInDto.getUserName());
        password.setText(signInDto.getPassword());

    }

}
