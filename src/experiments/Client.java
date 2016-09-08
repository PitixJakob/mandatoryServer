package experiments;

import experiments.gui.DummyClientGui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jakob on 08-09-16.
 */
public class Client{
    private String hostname;
    private int port;
    private String username;

    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader fromServer;

    private DummyClientGui dcg;

    public Client(DummyClientGui dcg){
        this.dcg = dcg;
    }




    public void connect(String hostname, int port, String username) throws IOException {
        socket = new Socket(hostname, port);
        toServer = new PrintWriter(socket.getOutputStream(), true);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        IncomingReader ir = new IncomingReader(this);
        Thread readerThread = new Thread(ir);
        readerThread.start();

        sendMessage("JOIN "+username+", "+hostname+":"+port);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getToServer() {
        return toServer;
    }

    public BufferedReader getFromServer() {
        return fromServer;
    }

    public void sendError(String errorMessage) throws IOException {
        dcg.showError(errorMessage);
        toServer.close();
        fromServer.close();
        socket.close();
    }

    public void sendMessage(String message){
        toServer.println(message);
    }

    public void getMessage(String message){
        dcg.getMessage(message);
    }
}
