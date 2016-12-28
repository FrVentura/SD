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

/**
 *
 * @author FrVentura
 */
public class HandlerAsyncClient implements Runnable {
    
    Socket cs;
    BufferedReader in;
    Locker locker;

    public HandlerAsyncClient(Socket s, BufferedReader br, Locker l) throws IOException, UnknownHostException{
        in = br;
        cs = s;
        locker = l;
        
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
