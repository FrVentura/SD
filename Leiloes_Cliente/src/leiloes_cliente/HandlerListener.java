package leiloes_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandlerListener implements Runnable{

    BufferedReader in;

    public HandlerListener(Socket s, BufferedReader br) throws IOException, UnknownHostException{
        in = br;
    }

    public void run(){
        /*
        try{
            while(true)
                
                System.out.println("--------------------------------------");
                String fromSv = in.readLine();
                System.out.println("");
                System.out.println("--------------------------------------");
                

           } catch (IOException ex) {
            Logger.getLogger(HandlerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
}
