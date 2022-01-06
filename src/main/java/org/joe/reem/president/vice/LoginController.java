package org.joe.reem.president.vice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController
{
    @FXML private Label displayMsg;
    @FXML private TextField username;
    @FXML private TextField password;
    private final StreamManager stream = new StreamManager();


    @FXML protected void onRegisterButtonClick(final ActionEvent event) throws IOException { switchScene(event, "Signup.fxml"); }
    @FXML protected void onSignInButtonClick(final ActionEvent event) throws IOException, ClassNotFoundException
    {
        //username field is empty
        if (username.getText().isEmpty() && !password.getText().isEmpty())
        {
            displayMsg.setText("Enter a username");
            displayMsg.setVisible(true); //show the text
        }

        //password field is empty
        else if (!username.getText().isEmpty() && password.getText().isEmpty())
        {
            displayMsg.setText("Enter a password");
            displayMsg.setVisible(true);
        }

        //both fields are empty
        else if (username.getText().isEmpty() && password.getText().isEmpty())
        {
            displayMsg.setText("Fill in the missing fields");
            displayMsg.setVisible(true);
        }
        else
        {
            JabberMessage reply = stream.exchange("signin " + username.getText() + " " + password.getText()); //send the message and get the reply from the server

            if (reply.getMessage().equals("signedin")) switchScene(event, "MainPage.fxml"); //user signed in, switch the scene to the main page
            else if (reply.getMessage().equals("incorrect-pass"))
            {
                displayMsg.setText("Incorrect Password. Try again");
                displayMsg.setVisible(true);
            }
            else
            {
                displayMsg.setText("Action failed. This might be because you don't have an account. Try signing up!");
                displayMsg.setVisible(true);
            }
        }
    }

    /**
     * Hides the current scene and switches to the new scene
     * @param event successful sign-in or sign-up
     */
    private void switchScene(final ActionEvent event, final String page) throws IOException
    {
        ((Node)event.getSource()).getScene().getWindow().hide(); //hide the current scene
        var fxmlLoader = new FXMLLoader(ClientMain.class.getResource(page)); //get the fxml file info
        var scene = new Scene(fxmlLoader.load()); //feed the fxml file info into the scene

        var stage = new Stage(); //instantiate new stage object

        if (page.equals("MainPage.fxml")) stage.setTitle("Welcome " + username.getText()); //setting the toolbar title
        else if (page.equals("Signup.fxml")) stage.setTitle("Your username is going to be your first name and last name mashed together");

        stage.setResizable(false); //not allowing stage to be resized
        stage.setScene(scene); //add the scene to the stage

        stage.show(); //show the stage
    }
}