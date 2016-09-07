package model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by jakob on 07-09-16.
 */
public class Server {
    private ArrayList<User> userList;
    private ArrayList<Message> messageList;
    private ArrayList<Socket> socketList;
    private Timer timer;
    private ServerSocket serverSocket;
}
