/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author FrVentura
 */
public class Handler implements Runnable {
    
    Socket mySocket;
    PrintWriter out;
    BufferedReader in;
    Leiloeira myLeiloeira;

    public Handler(Socket cs, Leiloeira lei) throws IOException{
        mySocket = cs;
        out = new PrintWriter (cs.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        myLeiloeira = lei;
    }

    public void run(){
        String curr;
        String usn = "none";
        String pwd;
        Vendedor vend = null;
        Comprador comp = null;
        int choice;
        int situation=0; // variavel que define a situacao do utilizador no final do register/login
        try{
            if( (curr = in.readLine()) != null);
                choice = Integer.parseInt(curr);
                

            switch (choice){
                case 1:
                        out.println("from server: a registar novo vendedor");
                        usn = in.readLine();
                        pwd = in.readLine();
                        vend = new Vendedor(usn,pwd);
                        if (myLeiloeira.addUtilizador(vend) == true){
                            out.println("from server: Success, username is \""+ usn + "\" password is \""+pwd+"\"");
                            out.println("situation1");
                            situation = 1;
                        }
                        else{
                            out.println("from server: Fail");
                            situation = -1;
                        }
                        break;
                case 2:
                        out.println("from server: a registar novo comprador");
                        usn = in.readLine();
                        pwd = in.readLine();
                        comp = new Comprador(usn,pwd);
                        if (myLeiloeira.addUtilizador(comp) == true){
                            out.println("from server: Success, username is \""+ usn + "\" password is \""+pwd+"\"");
                            situation = 2;
                        }
                        else{
                            out.println("from server: Fail");
                            situation = -1;
                        }
                        break;
                case 3:
                        out.println("from server: a fazer login como vendedor");
                        usn = in.readLine();
                        pwd = in.readLine();
                        if (myLeiloeira.authenticate(usn,pwd,1) == true){
                            out.println("from server: bem-vindo");
                            situation = 3;
                        }
                        else{
                            out.println("from server: Fail");
                            situation = -1;
                        }
                        break;
                case 4:
                        out.println("from server: a fazer login como comprador");
                        usn = in.readLine();
                        pwd = in.readLine();
                        if (myLeiloeira.authenticate(usn,pwd,2) == true){
                            out.println("from server: bem-vindo");
                            situation = 4;
                        }
                        else{
                            out.println("from server: Fail");
                            situation = -1;
                        }
                default:
                    break;
            }

            if (situation==1 || situation==3){
                String item;
                int valor;
                int numL = -1;

                if( (curr = in.readLine()) != null);
                    choice = Integer.parseInt(curr);
                if (choice == 1)
                    out.println("from server: a Listar leiloes");
                else if (choice == 2){
                    out.println("from server: iniciar novo leilao");
                    item = in.readLine();
                    System.out.println(item);

                    valor = Integer.parseInt(in.readLine());
                    System.out.println(valor);
                    numL=myLeiloeira.addLeilao(item,vend,valor);
                    out.println("from server: Leilao com numero "+numL+" iniciado com sucesso");
                }
                else if (choice == 3)
                    out.println("from server: finalizar leilao");
            }
        }

        catch (IOException e){};
    }

}
