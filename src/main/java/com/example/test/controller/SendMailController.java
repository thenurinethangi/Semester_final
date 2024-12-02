package com.example.test.controller;

import com.example.test.dto.TenantDto;
import com.example.test.dto.tm.HouseStatusCheckTm;
import com.example.test.model.TenantModel;
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
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

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

    private final TenantModel tenantModel = new TenantModel();
    private HouseStatusCheckTm selectedHouseStatusCheckToSendMail;
    private TenantDto tenant;

    @FXML
    void exitOnMouseClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void clearOnMouseClicked(MouseEvent event) {
        messageArea.clear();
    }

    @FXML
    void sendBtnOnAction(ActionEvent event) {
        String recipientEmail = tenant.getEmail();
        String subject = subjectTxt.getText();
        String msg = messageArea.getText();

        if (subject.isEmpty() || msg.isEmpty()) {
            return;
        }


        new Thread(() -> sendMail(recipientEmail, subject, msg)).start();

        Notifications notifications = Notifications.create();
        notifications.title("Notification");
        notifications.text("Sent Email To The Tenant ID: "+selectedHouseStatusCheckToSendMail.getTenantId());
        notifications.hideCloseButton();
        notifications.hideAfter(Duration.seconds(5));
        notifications.position(Pos.CENTER);
        notifications.darkStyle();
        notifications.showInformation();
    }

    public void sendMail(String recipientEmail, String subject, String msg) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "thenurinathangi@gmail.com";
        String password = "hkzu guyn ahxq hlmh";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO, new Address[]{
                    new InternetAddress(recipientEmail)
            });
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error while sending email to Tenant ID: "+selectedHouseStatusCheckToSendMail.getTenantId() + e.getMessage());
        }
    }

    public void prepareMail(HouseStatusCheckTm selectedHouseCheck) {

        selectedHouseStatusCheckToSendMail = selectedHouseCheck;

        try {
            tenant = tenantModel.getMoreTenantDetails(selectedHouseCheck.getTenantId());
            receiverEmailAddress.setText(tenant.getEmail());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        subjectTxt.setText("Regarding Payment for Damaged Property In Latest Inspection");

        messageArea.setText("I would like to bring to your attention the matter of payment \nrelated to the damage identified during the recent inspection of our property. \nYour prompt attention to this issue is greatly appreciated, \nand I welcome the opportunity to discuss the specifics at your earliest convenience.\n" +
                "\n" +
                "In the latest inspection of House ID: " + selectedHouseCheck.getHouseId() + ", \nwe discovered considerable damages that could not be overlooked. \nWe initially considered utilizing the security fund to cover the repair costs. \nhowever, the available amount is insufficient. Consequently, \nwe respectfully request a payment of Rs. " + selectedHouseCheck.getEstimatedCostForRepair() + " to be processed at your earliest convenience.\n" +
                "\n" +
                "Thank you for your cooperation and understanding in this matter.\n\n" +
                "The GrandView Residences\nColombo 08");
    }
}
