/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FrVentura
 */
public class HandlerAssynchronous implements Runnable {
    
    Socket mySocket;
    PrintWriter out;
    Leiloeira myLeiloeira;
    String username;
    
    public HandlerAssynchronous(Socket cs, Leiloeira lei, String usn) throws IOException{
        mySocket = cs;
        out = new PrintWriter (cs.getOutputStream(), true);
        myLeiloeira = lei;
        username = usn;
    }
    
        public void run(){
            try {
                boolean bool=false;
                while(bool==false){
                    bool = myLeiloeira.esperarPorHistorico(username);
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(HandlerAssynchronous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}