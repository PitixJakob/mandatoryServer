package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by jakob on 07-09-16.
 */
public class Server implements Runnable{
    private ArrayList<User> userList;
    private ArrayList<Message> messageList;
    private ArrayList<Socket> socketList;
    private Timer timer;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        userList = new ArrayList<>();
        messageList = new ArrayList<>();
        socketList = new ArrayList<>();
        timer = new Timer();
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(32003);
            System.out.println("Server started, waiting for clients");

            while (true){
                Socket clientSocket = serverSocket.accept();
                User user = new User(clientSocket, new PrintWriter(clientSocket.getOutputStream()));
                userList.add(user);
                Thread userListener = new Thread(user);
                userListener.start();
                System.out.println("User connected");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
