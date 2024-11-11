package com.example.test.controller;

import com.example.test.dto.SignInDto;
import com.example.test.dto.SignInQuestionsDto;
import com.example.test.model.ForgotPasswordQuestionsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPasswordQuestionsController {

    @FXML
    private TextField fquestion;

    @FXML
    private TextField squestion;

    @FXML
    private TextField tquestion;

    @FXML
    private Button submit;

    @FXML
    private Button back;

    private Stage stage;

    private String userName;
    private SignInController signInController;
    private ForgotPasswordQuestionsModel forgotPasswordQuestionsModel;

    public ForgotPasswordQuestionsController(){

        signInController = new SignInController();
        try {
            forgotPasswordQuestionsModel = new ForgotPasswordQuestionsModel();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    @FXML
    void backOnAction(ActionEvent event) {

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void submitOnAction(ActionEvent event) throws IOException {

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

           SignInQuestionsDto signInQuestionsDto = new SignInQuestionsDto(fAnswer,sAnswer,tAnswer);
           signInQuestionsDto.setUserName(userName);

           try {
               String result = forgotPasswordQuestionsModel.getPassword(signInQuestionsDto);

               if(!result.equals("This User Name does not exit") && !result.equals("Something wrong with getting password back, try again later") && !result.equals("Can't get password because your answers are incorrect")){

                   SignInDto signInDto = new SignInDto(userName,result);

                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
                   Parent root = fxmlLoader.load();

                   SignInController controller = fxmlLoader.getController();
                   controller.setData(signInDto);

                   Scene scene = new Scene(root);
                   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                   stage.setScene(scene);
                   stage.centerOnScreen();
               }
               else{

                   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                   stage.close();

                   Alert alert = new Alert(Alert.AlertType.INFORMATION);
                   alert.setTitle("Information");
                   alert.setHeaderText("Something wrong with getting your password");
                   alert.setContentText(result);
                   alert.showAndWait();
               }
           }
           catch (Exception e){
               e.printStackTrace();
           }

        }
        clear();
    }


    public void clear(){

        fquestion.setText("");
        squestion.setText("");
        tquestion.setText("");
    }

    public void setUserName(String u){

        this.userName = u;
    }

}








