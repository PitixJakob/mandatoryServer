package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakob on 07-09-16.
 */
public class Server implements Runnable {
    private ArrayList<User> users;
    private ServerSocket serverSocket;
    private int port;

    /**
     * Initial server setup
     * @throws IOException
     */
    public Server(int port) {
        users = new ArrayList<>();
        this.port = port;
    }

    /**
     * Sends a message to all user
     * @param message
     */
    public void broadcast(String message) {
        System.out.println(message);

        for (User user : users) {
            user.receiveMessage(message);
        }
    }

    /**
     * Checks if User is already on the list, if so, returns false, if not, returns true and adds the user to the list
     * @param user
     * @return if successful, returns true
     */
    public boolean addUser(User user){
        boolean result = true;

        //Checks if user is already on the list
        for (User listUser : users) {
            if (user.getUsername().equals(listUser.getUsername())){
                result = false;
            }
        }

        //If user is not on the list, adds user to the users list and sends "J_OK" to user
        if (result){
            users.add(user);
            broadcast("DATA User: "+user.getUsername()+" joined the chat");
            user.receiveMessage("J_OK");
            updateList();
            return true;
        }else{ // If user is on the list, server responds with "J_ERR"
            user.receiveMessage("J_ERR");
            return false;
        }
    }

    /**
     * removes a user from the list of connected users
     * @param user
     */
    public void removeUser(User user){
        User userToBeRemoved = null;
        for (User listUser : users) {
            if (listUser.getUsername().equals(user.getUsername())){
                userToBeRemoved = listUser;
            }
        }
        if (userToBeRemoved != null){
            broadcast("DATA User: "+userToBeRemoved.getUsername()+" disconnected");
            users.remove(userToBeRemoved);
            updateList();
        }
    }

    /**
     * Updates connected users list and broadcasts the update
     */
    public void updateList(){
        String list = "LIST";
        for (User user : users) {
            list += " "+user.getUsername();
        }
        broadcast(list);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, listening on port: "+port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                User user = new User(clientSocket, this);
                Thread userListener = new Thread(user);
                userListener.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
