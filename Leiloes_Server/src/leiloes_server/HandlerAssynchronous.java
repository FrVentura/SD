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
    boolean state;
    ObjState state2;

    
    public HandlerAssynchronous(Socket cs, Leiloeira lei, String usn) throws IOException{
        mySocket = cs;
        out = new PrintWriter (cs.getOutputStream(), true);
        myLeiloeira = lei;
        username = usn;
        state = true;
        state2 = new ObjState(true);


    }
    
    public void changeState(){

        state = false;
        synchronized(state2){
        state2.csTfalse();
        } 
    }
         
    
    public void run(){

        StringBuilder s = new StringBuilder();

        try{

            while(state2.getState()){

                s = myLeiloeira.esperarPorHistorico(username,state2);

                if(state2.getState()){
                    String tmp = new String();
                    String[] lines = s.toString().split("\\n");

                    for (String st : lines){
                        tmp+=st+",";
                    }
                    out.println(tmp);
                }
            }
       }catch(InterruptedException e){}
    }
}

