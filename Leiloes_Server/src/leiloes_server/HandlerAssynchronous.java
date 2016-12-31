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
import java.io.BufferedReader;

import java.io.InputStreamReader;


/**
 *
 * @author FrVentura
 */
public class HandlerAssynchronous implements Runnable {
    
    Socket mySocket;
    PrintWriter out;
    BufferedReader in ;
    Leiloeira myLeiloeira;
    String username;
    
    
    
    
    
    public HandlerAssynchronous(Socket cs, Leiloeira lei, String usn) throws IOException{
        mySocket = cs;
        out = new PrintWriter (cs.getOutputStream(), true);
        myLeiloeira = lei;
        username = usn;
        
        
    }
                // A RESOLVER PELO RENATO -> AVISAR COMPRADORES QUE LICITARAM (dizer qual o idLeilao)
                //                          -> AVISAR VENCEDOR DO LEILAO (dizer qual o idLeilao)
    
        public void run(){
            
            StringBuilder s;
            
        
            
            try{
           
               while(true){
            
               s = new StringBuilder();
              
               s = myLeiloeira.esperarPorHistorico(username);
              
              
               
              // Esta string tem que ser recebida e tratada do lado cliente
              //System.out.println(s);

              
              String tmp = new String();
              String[] lines = s.toString().split("\\n");
              
              for (String st : lines){
                  tmp+=st+",";
              }
              
             
              out.println(tmp);
                
            }
           }catch(InterruptedException e){
           
           }
        }
            
            
            
            /*
               try {
                boolean bool=false;
                while(bool==false){
                    bool = myLeiloeira.esperarPorHistorico(username);
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(HandlerAssynchronous.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
}

