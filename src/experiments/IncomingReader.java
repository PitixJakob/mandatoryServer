package experiments;

import java.io.BufferedReader;

/**
 * Created by jakob on 07-09-16.
 */
public class IncomingReader implements Runnable{
    private BufferedReader reader;

    public IncomingReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        String message;

        try{
            while ((message = reader.readLine()) != null){
                System.out.println(message);

            }
        }catch (Exception ex){

        }
    }
}
