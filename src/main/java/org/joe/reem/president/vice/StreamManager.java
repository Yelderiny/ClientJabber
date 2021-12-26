package org.joe.reem.president.vice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StreamManager
{
    private final Socket socket = SocketConnection.getSocket();

    /**
     * Exchange carries out the whole process of sending a message and receiving a reply from the server
     * @param message user command
     * @return the JabberMessage object that the server returns
     */
    public JabberMessage exchange(final String message) throws IOException, ClassNotFoundException
    {
        sendMessage(message);
        return getReply();
    }

    /**
     * Sends a message to the server
     * @param message user command
     */
    private void sendMessage(final String message) throws IOException
    {
        var outStream = new ObjectOutputStream(socket.getOutputStream());

        outStream.writeObject(new JabberMessage(message));
        outStream.flush();
    }

    /**
     * Gets the reply from the server
     * @return the JabberMessage object that the server returns
     */
    private JabberMessage getReply() throws IOException, ClassNotFoundException
    {
        var inStream = new ObjectInputStream(socket.getInputStream());
        return (JabberMessage) inStream.readObject();
    }
}
