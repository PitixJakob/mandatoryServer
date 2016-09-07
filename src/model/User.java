package model;

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
    private BufferedReader reader;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;

    public User(Socket socket, PrintWriter pw) {
        this.socket = socket;
        this.pw = pw;
        try {
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(isReader);
        } catch (IOException e) {
            System.out.println("an unexpected error occured");
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
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
        String connect = "Connect";
        String disconnect = "Disconnect";
        String chat = "Chat";

        String[] data;

        try{
            while ((message = reader.readLine()) != null){
                System.out.println("Message");
            }
        }catch (Exception ex){

        }
    }
}
