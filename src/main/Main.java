package main;

import model.Server;
import java.util.Scanner;

/**
 * Created by jakob on 07-09-16.
 */
public class Main {
    public static void main(String[] args) {
        Thread server = new Thread(new Server(queryForPort()));
        server.start();
    }

    /**
     * Asks user for port number
     * @return
     */
    public static int queryForPort() {
        System.out.println("Please enter a port number (Press enter to use default port 5555)");

        Scanner scanner = new Scanner(System.in);
        String scanInput = scanner.nextLine();
        int result = 5555;
        if (!scanInput.isEmpty()) {
            try {
                result = Integer.parseInt(scanInput);
            } catch (NumberFormatException ex) {
                result = queryForPort();
            }
        }
        return result;
    }
}
