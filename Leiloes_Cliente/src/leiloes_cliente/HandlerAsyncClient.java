/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_cliente;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
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
    Socket cs;
    PrintWriter out;
    

    public HandlerAsyncClient(Socket s, BufferedReader br, Locker l) throws IOException, UnknownHostException{
        in = br;
        cs = s;
        locker = l;
        
               
        
    }

    @Override
    public void run() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        try{
            Thread listen = new Thread (new HandlerListener(cs,br,locker));
            listen.start();
            
            String fromSv;
            while(true){
                fromSv = in.readLine();
                out.println(fromSv);

            }

        }
        catch (IOException ex) {
            Logger.getLogger(HandlerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
