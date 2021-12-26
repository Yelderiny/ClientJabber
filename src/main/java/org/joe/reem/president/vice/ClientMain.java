package org.joe.reem.president.vice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ClientMain extends Application
{
    @Override
    public void start(final Stage stage) throws IOException
    {
        SocketConnection.setSocket(new Socket("localhost", 44445)); //establishing socket connection

        var fxmlLoader = new FXMLLoader(ClientMain.class.getResource("Login.fxml")); //get the fxml file info
        var scene = new Scene(fxmlLoader.load()); //feed the fxml file info into the scene

        stage.setTitle("Jabber!"); //setting the toolbar title
        stage.setResizable(false); //not allowing stage to be resized
        stage.setScene(scene); //add the scene to the stage

        stage.show(); //show the stage
    }

    public static void main(String[] args) { launch(); }
}