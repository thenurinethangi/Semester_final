package com.example.test.controller;

import com.example.test.dto.tm.LeaseAgreementTm;
import com.example.test.model.LeaseAgreementModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import java.sql.SQLException;

public class MailSendFormInLeaseAgreementController {

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
    private Button sendWithAttachmentBtn;

    @FXML
    private Button send;

    private final LeaseAgreementModel leaseAgreementModel = new LeaseAgreementModel();

    @FXML
    void clear(MouseEvent event) {

        messageArea.clear();
    }

    @FXML
    void exitOnMouseClicked(MouseEvent event) {

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void sendBtnOnAction(ActionEvent event) {

        String recipientEmail = receiverEmailAddress.getText();
        String subject = subjectTxt.getText();
        String msg = messageArea.getText();

        sendMail(recipientEmail,subject,msg);
    }


    public void sendMail(String recipientEmail,String subject,String msg){

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myEmail = "thenurinathangi@gmail.com";
        String password = "hkzu guyn ahxq hlmh";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail,password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO,new Address[]{
                    new InternetAddress(recipientEmail)
            });
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Successfully Send Email To Tenant");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
            notifications.showInformation();

        }
        catch (MessagingException e) {

            Notifications notifications = Notifications.create();
            notifications.title("Notification");
            notifications.text("Something Went Wrong With Sending A Email, Try Again Later");
            notifications.hideCloseButton();
            notifications.hideAfter(Duration.seconds(5));
            notifications.position(Pos.CENTER);
            notifications.darkStyle();
        }

    }


    @FXML
    void sendWithAttachmentOnAction(ActionEvent event) {

    }

    public void setSelectedTenantDetailsToSendMail(LeaseAgreementTm selectedLeaseAgreement,String sub,String message) {
        subjectTxt.setText(sub);
        messageArea.setText(message);

        try {
            String email = leaseAgreementModel.getTenantEmail(selectedLeaseAgreement);
            receiverEmailAddress.setText(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
