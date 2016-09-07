package main;

import model.Server;

import java.io.IOException;

/**
 * Created by jakob on 07-09-16.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Thread server = new Thread(new Server());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
