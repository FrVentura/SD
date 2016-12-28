/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FrVentura
 */
public class HandlerAsyncClient implements Runnable {
    
    BufferedReader in;
    Locker locker;

    public HandlerAsyncClient(BufferedReader br) throws IOException, UnknownHostException{
        in = br;
        
    }

    @Override
    public void run() {
        try{
            
            String fromSv;
            while(true){
                fromSv = in.readLine();
                System.out.println("from async: " +fromSv);

            }

        }
        catch (IOException ex) {
            Logger.getLogger(HandlerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
