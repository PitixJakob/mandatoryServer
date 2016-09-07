package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jakob on 07-09-16.
 */
public class User implements Runnable {
    private String user;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader fromUser;
    private Server server;
    private Timer timer;

    public User(Socket socket, PrintWriter pw, Server server) {
        this.server = server;
        this.socket = socket;
        this.pw = pw;
        try {
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            fromUser = new BufferedReader(isReader);
        } catch (IOException e) {
            System.out.println("an unexpected error occured");
        }
        timer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
    }

    public String getUser() {
        String result = "User not found";
        if (user != null){
            result = user;
        }
        return result;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void run() {
        String message;

        try{
            while ((message = fromUser.readLine()) != null){
                if (message.startsWith("JOIN")){
                    createUser(message.substring(5));
                }
                if (message.startsWith("DATA")){
                    sendMessage(message.substring(5));
                }
                if (message.startsWith("ALVE")){
                    stillAlive();
                }
                if (message.startsWith("QUIT")){
                    disconnect();
                }


            }
        }catch (Exception ex){

        }
    }

    public void createUser(String message){
        String[] data = message.split(",");
        setUser(data[0]);
        server.addUser(this);
        timer.start();
    }

    public void sendMessage(String message){
        server.broadcast(message);
    }

    public void getMessage(String message){
        pw.println(message);
    }

    public void stillAlive(){
        timer.restart();
    }

    public void disconnect(){
        server.removeUser(this);
        timer.stop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
