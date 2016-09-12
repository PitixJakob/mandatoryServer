package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakob on 07-09-16.
 */
public class Server implements Runnable {
    private ArrayList<User> users;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        users = new ArrayList<>();
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(60000);
            System.out.println("Server started, waiting for clients");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                User user = new User(clientSocket, new PrintWriter(clientSocket.getOutputStream(), true), this);
                Thread userListener = new Thread(user);
                userListener.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void broadcast(String message) {
        System.out.println(message);

        for (User user : users) {
            user.getMessage(message);
        }
    }

    public boolean addUser(User user){
        boolean result = true;

        for (User listUser : users) {
            if (user.getUser().equals(listUser.getUser())){
                result = false;
            }
        }
        if (result){
            users.add(user);
            broadcast("DATA User: "+user.getUser()+" joined the chat");
            user.getMessage("J_OK");
            updateList();
            return true;
        }else{
            user.getMessage("J_ERR");
            return false;
        }
    }

    public void removeUser(User user){
        User userToBeRemoved = null;
        for (User listUser : users) {
            if (listUser.getUser().equals(user.getUser())){
                userToBeRemoved = listUser;
            }
        }
        if (userToBeRemoved != null){
            broadcast("DATA User: "+userToBeRemoved.getUser()+" disconnected");
            users.remove(userToBeRemoved);
            updateList();
        }
    }

    public void updateList(){
        String list = "LIST";
        for (User user : users) {
            list += " "+user.getUser();
        }

        broadcast(list);
    }

}
