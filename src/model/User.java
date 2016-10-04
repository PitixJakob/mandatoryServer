package model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jakob on 07-09-16.
 */
public class User implements Runnable {
    private String username;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    private Timer timer;

    /**
     * Initial connection setup
     *
     * @param socket
     * @param server
     */
    public User(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isReader);
        } catch (IOException e) {
            System.out.println("an unexpected error occured");
        }
        timer = new Timer(70000, e -> disconnect());
    }

    /**
     * Creates username if the the username is not already on the username list.
     *
     * @param message
     */
    public void createUser(String message) {
        String[] data = message.split(",");
        setUsername(data[0]);
        if (server.addUser(this)) {
            timer.start();
        } else {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Broadcasts a message to all users
     * @param message
     */
    public void receiveMessageFromUser(String message) {
        if (message.length() <= 255) {
            server.broadcast(message);
        }
    }

    /**
     * Sends a message from the server to the this user
     * @param message
     */
    public void sendMessageToUser(String message) {
        out.println(message);
    }

    /**
     * Disconnects this user from the server
     */
    public void disconnect() {
        out.println("Disconnected from server");
        if (out != null) {
            out.close();
        }
        try {
            if (in != null) {
                in.close();
            }
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (IOException e) {
            //TRYING TO DISCONNECT ANYWAYS
        }
        server.removeUser(this);
        timer.stop();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Run method from Runnable used to receive messages via the BufferedReader in
     */
    @Override
    public void run() {
        String message;

        try {
            // While loop for receiving messages from this user
            while ((message = in.readLine()) != null) {
                //Receiving different messages and check for the protocol
                if (message.startsWith("JOIN")) {
                    createUser(message.substring(5));
                }
                if (message.startsWith("DATA")) {
                    receiveMessageFromUser(message);
                }
                if (message.startsWith("ALVE")) {
                    System.out.println("RECEIVED ALVE FROM USER: "+username);
                    timer.restart();
                }
                if (message.startsWith("QUIT")) {
                    System.out.println("RECEIVED QUIT FROM USER: "+username);
                    disconnect();
                }
            }
        } catch (IOException e) {
            //Only occurs when user disconnects, which is fine.
        }
    }


}
