/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FrVentura
 */
public class Leiloes_Server {
    
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(9999);
        Socket cs = null;
        Leiloeira myLeiloeira = new Leiloeira();
        
        try{
            FileInputStream fis = new FileInputStream("leiloeira.dat");
            myLeiloeira.loadLeiloeira();
        }
        catch(FileNotFoundException | ClassNotFoundException e){}

        while(true){
            cs = ss.accept();
            (new Thread (new Handler(cs,myLeiloeira))).start();
  
        }
        
    }

}
