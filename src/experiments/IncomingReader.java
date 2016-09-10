package experiments;

import java.io.BufferedReader;

/**
 * Created by jakob on 07-09-16.
 */
public class IncomingReader implements Runnable{
    private Client client;
    private BufferedReader fromServer;

    public IncomingReader(BufferedReader fromServer) {
        this.fromServer = fromServer;
    }

    public IncomingReader(Client client) {
        this.client = client;
        this.fromServer = client.getFromServer();
    }

    @Override
    public void run() {
        String message;

        try{
            while ((message = fromServer.readLine()) != null){
                if (message.startsWith("J_ERR")){
                    client.sendError("Username is already taken");
                }
                if (message.startsWith("DATA ")){
                    client.getMessage(message.substring(5));
                }
                if (message.startsWith("LIST ")){
                    String[] result = message.substring(5).split(" ");
                    client.updateListedUsers(result);
                }
                if (message.startsWith("J_OK")){
                    client.joinOK();
                }

            }
        }catch (Exception ex){

        }
    }
}
