/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author FrVentura
 */
public class Leiloes_Cliente {

    public static int mode;
    
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //Socket cs = new Socket("127.0.0.1", 9999);
        //PrintWriter out = new PrintWriter (cs.getOutputStream(), true);
        //BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        String tmp;
        String curr;
        
        
        do{
            Runtime.getRuntime().exec("clear");
            //System.out.print('\u000C');
            System.out.println("Prima 1 para se registar como novo vendedor");
            System.out.println("Prima 2 para se registar como novo comprador");
            System.out.println("Prima 3 para fazer login como vendedor");
            System.out.println("Prima 4 para fazer login como comprador");
            System.out.println("Digite \"end\" para sair");
            System.out.println("---------------------------------------------");
            tmp=sin.readLine();
            
            
            switch(tmp){
                case "1":
                    //out.println(tmp);
                    System.out.println("Introduza um username");
                    curr = sin.readLine();
                    //out.println(curr);
                    System.out.println("Introduza uma password");
                    curr = sin.readLine();
                    //out.println(curr);
                    break;
                
                case "2":
                    //out.println(tmp);
                    System.out.println("Introduza um username");
                    curr = sin.readLine();
                    //out.println(curr);
                    System.out.println("Introduza uma password");
                    curr = sin.readLine();
                    //out.println(curr);
                    break;
                    
                case "3":
                    //out.println(tmp);
                    System.out.println("Introduza o seu username");
                    curr = sin.readLine();
                    //out.println(curr);
                    System.out.println("Introduza a sua password");
                    curr = sin.readLine();
                    //out.println(curr);
                    break;
                    
                case "4":
                    //out.println(tmp);
                    System.out.println("Introduza o seu username");
                    curr = sin.readLine();
                    //out.println(curr);
                    System.out.println("Introduza a sua password");
                    curr = sin.readLine();
                    //out.println(curr);
                    break;
                
                default :
                    break;
            }
        }
        while(!tmp.equals("end"));
        
        
        
        
    }
    
}
