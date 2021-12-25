package org.joe.reem.president.vice;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateTimeline extends ScheduledService<Void>
{
    private final StreamManager stream = new StreamManager();
    private final VBox timelineBox;

    public UpdateTimeline(VBox timelineBox) {this.timelineBox = timelineBox;}

    @Override
    protected Task<Void> createTask()
    {
        return new Task<>()
        {
            @Override
            protected Void call()
            {
                Platform.runLater(() ->
                {
                    try { resetTimeline(); }
                    catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
                });
                return null;
            }
        };
    }

    /**
     * Resets the timeline
     */
    private void resetTimeline() throws IOException, ClassNotFoundException
    {
        timelineBox.getChildren().clear(); //clear the timeline
        setTimeline(getServerInfo()); //reset the timeline
    }

    /**
     * For every post in the timeline, extract its elements, make a space for it on the GUI and display it
     * @param timelineInfo arraylist of all timeline information
     */
    private void setTimeline(final ArrayList<ArrayList<String>> timelineInfo)
    {
        Platform.runLater(() ->
        {
            for (ArrayList<String> arr : timelineInfo)
            {
                //extract elements
                String username = arr.get(0);
                String jab = arr.get(1);
                String jabID =arr.get(2);
                AtomicInteger likes = new AtomicInteger(Integer.parseInt(arr.get(3)));

                //create jab label
                Label user = new Label("      " + username + ": " + jab + "     ");
                user.setId(jabID);
                user.prefHeight(50);
                user.setStyle("-fx-wrap-text: true"); //if longer than width, go to the next line

                //create like button
                Button like = new Button(likes + " like");
                like.setId(jabID);

                HBox userJab = new HBox();
                userJab.getChildren().add(user); //add label to Hbox
                HBox.setHgrow(user, Priority.ALWAYS); //grows according to the size of the contents within

                Region region = new Region();
                region.setPrefWidth(1); //create a space between the label and the button

                userJab.getChildren().addAll(region, like); //add region and like button
                HBox.setHgrow(like, Priority.ALWAYS); //grows according to the size of the contents within

                timelineBox.getChildren().add(userJab); //add the Hbox to the Vbox
                VBox.setVgrow(userJab, Priority.ALWAYS); //grows according to the size of the contents within

                //create a listener for like button if clicked
                like.setOnAction(actionEvent ->
                {
                    String reply = "";

                    try { reply = getServerMessage("like " + like.getId()); } //tell the server that the jab has been liked
                    catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

                    //the reply is posted
                    if (reply.equals("posted"))
                    {
                        likes.getAndIncrement(); //increment the likes
                        like.setText(likes + " like"); //change the button
                    }
                });
            }
        });
    }

    /**
     * Sends a message to the server and receive the reply. Then use the reply to extract the data from it
     * @return the information of the reply
     */
    private ArrayList<ArrayList<String>> getServerInfo() throws IOException, ClassNotFoundException
    {
        JabberMessage reply = stream.exchange("timeline");
        return reply.getData();
    }

    /**
     * Sends a message to the server and receive the reply. Then use the reply to extract the message from it
     * @param message the message sent to the server
     * @return the message part of the reply
     */
    private String getServerMessage(final String message) throws IOException, ClassNotFoundException
    {
        JabberMessage reply = stream.exchange(message);
        return reply.getMessage();
    }
}
