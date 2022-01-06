package org.joe.reem.president.vice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController
{
    @FXML private TextField username;
    @FXML private TextField eMail;
    @FXML private TextField pass;
    @FXML private Label notice;

    private final StreamManager stream = new StreamManager();

    @FXML
    protected void onBackButtonClick(final ActionEvent event) throws IOException { switchScene(event); }
    @FXML
    protected void onSignUpButtonClick(final ActionEvent event) throws IOException, ClassNotFoundException
    {
        //fields are left empty
        if (username.getText().isEmpty() || eMail.getText().isEmpty() || pass.getText().isEmpty())
        {
            notice.setText("Fill in all the missing fields");
            notice.setAlignment(Pos.CENTER);
            notice.setVisible(true);
        }

        //email doesn't contain an "@" symbol
        if (!eMail.getText().contains("@"))
        {
            notice.setText("Please enter a valid e-Mail");
            notice.setAlignment(Pos.CENTER);
            notice.setVisible(true);
        }
        else
        {
            JabberMessage reply = stream.exchange("register " + username.getText() + " " + eMail.getText() + " " + pass.getText()); //send the message and get the reply from the server

            //investigate the reply
            switch (reply.getMessage())
            {
                case "registered" -> switchScene(event); //new user registered, switch the scene to the main page

                //user already registered
                case "already-registered" ->
                {
                    notice.setText("This username is already registered");
                    notice.setAlignment(Pos.CENTER);
                    notice.setVisible(true);
                }
                //email already in use
                case "eMail-already-in-use" ->
                {
                    notice.setText("This e-Mail is already registered");
                    notice.setAlignment(Pos.CENTER);
                    notice.setVisible(true);
                }
            }
        }
    }

    /**
     * Hides the current scene and switches to the new scene
     * @param event successful sign-in or sign-up
     */
    private void switchScene(final ActionEvent event) throws IOException
    {
        ((Node)event.getSource()).getScene().getWindow().hide(); //hide the current scene
        var fxmlLoader = new FXMLLoader(ClientMain.class.getResource("Login.fxml")); //get the fxml file info
        var scene = new Scene(fxmlLoader.load()); //feed the fxml file info into the scene

        var stage = new Stage(); //instantiate new stage object
        stage.setTitle("Jabber!"); //setting the toolbar title
        stage.setResizable(false); //not allowing stage to be resized
        stage.setScene(scene); //add the scene to the stage

        stage.show(); //show the stage
    }
}
