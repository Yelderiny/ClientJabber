package org.joe.reem.president.vice;

import java.net.Socket;

public final class  SocketConnection
{
    private static Socket socket;

    //accessors & mutators
    public static Socket getSocket() { return socket; }
    public static void setSocket(final Socket socket) { SocketConnection.socket = socket;}
}
