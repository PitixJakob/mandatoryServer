package experiments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by jakob on 07-09-16.
 */
public class DummyClient {
    private static BufferedReader in;

    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 60000;

        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            IncomingReader ir = new IncomingReader(in);
            Thread readerThread = new Thread(ir);
            readerThread.start();

            out.println("JOIN Jakob, 123:123");

            interaction(out);
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void interaction(PrintWriter out){
        out.println(new Scanner(System.in).nextLine());
        interaction(out);
    }
}
