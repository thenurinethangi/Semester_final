package com.example.test.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SendMailController {

    @FXML
    private ImageView exit;

    @FXML
    private ImageView clearBtn;

    @FXML
    private TextField receiverEmailAddress;

    @FXML
    private TextField subjectTxt;

    @FXML
    private TextArea messageArea;

    @FXML
    private Button sendBtn;


    @FXML
    void sendBtnOnAction(ActionEvent event) {

    }


    public void setSelectedTenantDetails(){


    }
}
