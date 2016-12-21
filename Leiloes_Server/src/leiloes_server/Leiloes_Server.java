/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author FrVentura
 */
public class Leiloes_Server {
    
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9999);
        Socket cs = null;
        Leiloeira myLeiloeira = new Leiloeira();

        while(true){
            cs = ss.accept();
            (new Thread (new Handler(cs,myLeiloeira))).start();
            
            
        }
        
    }

}
