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
import java.util.ArrayList;

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

    @Override
    public void run(){
        
        String curr;
        String usn = "none";
        String pwd;
        Vendedor vend = null;
        Comprador comp = null;
        int choice=0;  
        int situation=0; // variavel que define a situacao do utilizador no final do register/login
        try{

            while(situation<1 || situation > 4){
                choice = -1;
                
                while (choice<1 || choice>4){
                    if( (curr = in.readLine()) != null);
                        choice = Integer.parseInt(curr);
                }
                
                
                switch (choice){
                    case 1:
                            //out.println("from server: a registar novo vendedor");
                            usn = in.readLine();
                            pwd = in.readLine();
                            vend = new Vendedor(usn,pwd);
                            if (myLeiloeira.addUtilizador(vend) == true){
                                //out.println("from server: Success, username is \""+ usn + "\" password is \""+pwd+"\"");
                                out.println("situation1");
                                //System.out.println("situation1");
                                situation = 1;
                                (new Thread (new HandlerAssynchronous(mySocket,myLeiloeira,usn))).start();
                            }
                            else{
                                out.println("from server: Fail");
                                situation = -1;
                            }
                            break;
                    case 2:
                            //out.println("from server: a registar novo comprador");
                            usn = in.readLine();
                            pwd = in.readLine();
                            comp = new Comprador(usn,pwd);
                            if (myLeiloeira.addUtilizador(comp) == true){
                                //out.println("from server: Success, username is \""+ usn + "\" password is \""+pwd+"\"");
                                out.println("situation2");
                                situation = 2;
                                (new Thread (new HandlerAssynchronous(mySocket,myLeiloeira,usn))).start();
                            }
                            else{
                                out.println("from server: Fail");
                                situation = -1;
                            }
                            break;
                    case 3:
                            //out.println("from server: a fazer login como vendedor");
                            usn = in.readLine();
                            pwd = in.readLine();
                            if (myLeiloeira.authenticate(usn,pwd,1) == true){
                                //out.println("from server: bem-vindo");
                                out.println("situation3");
                                situation = 3;
                                (new Thread (new HandlerAssynchronous(mySocket,myLeiloeira,usn))).start();
                            }
                            else{
                                out.println("from server: Fail");
                                situation = -1;
                            }
                            break;
                    case 4:
                            //out.println("from server: a fazer login como comprador");
                            usn = in.readLine();
                            pwd = in.readLine();
                            if (myLeiloeira.authenticate(usn,pwd,2) == true){
                                //out.println("from server: bem-vindo");
                                out.println("situation4");
                                situation = 4;
                                (new Thread (new HandlerAssynchronous(mySocket,myLeiloeira,usn))).start();
                            }
                            else{
                                out.println("from server: Fail");
                                situation = -1;
                            }
                    default:
                        break;
                }
            }
            
            
            // declaracao de variaveis auxiliares
            choice = -1;
            int idLeilao = -1;
            double preco = -1;
            String item;
                    
            if (situation==1 || situation==3){ // caso do Vendedor :: nao sai daqui ate desconectar
                while (true){
                    if( (curr = in.readLine()) != null);
                        choice = Integer.parseInt(curr);

                    switch (choice){
                        case 1:
                            ArrayList<String> listaLeiloes = new ArrayList<>();
                            listaLeiloes = myLeiloeira.ListarLeiloes(usn);
                            out.println("from server: a Listar leiloes");
                            //System.out.println(usn);
                            for (String s : listaLeiloes){
                                out.println(s);
                                //System.out.println(s);
                            }
                            out.println("end");
                            break;

                        case 2:
                            if((curr = in.readLine()) != null);
                                item = curr;
                            if((curr = in.readLine()) != null);
                                preco = Double.parseDouble(curr);
                            idLeilao = myLeiloeira.addLeilao(item, usn, preco);
                            out.println(""+idLeilao);
                            break;

                        case 3:
                            //out.println("from server: a finalizar leilao: ");
                            if((curr = in.readLine()) != null);
                                idLeilao = Integer.parseInt(curr);
                            if (myLeiloeira.fecharLeilao(idLeilao, usn)){
                                out.println(idLeilao);
                            }
                            break;
                        default:
                            break;
                    }    
                }
            }
            else if (situation == 2 || situation == 4){ // Caso do comprador :: nao sai daqui ate desconectar
                while (true){
                    
                    if( (curr = in.readLine()) != null);
                        choice = Integer.parseInt(curr);

                    switch (choice){
                        case 1:
                            ArrayList<String> listaLeiloes = new ArrayList<>();
                            listaLeiloes = myLeiloeira.ListarLeiloes(usn);
                            out.println("from server: a Listar leiloes");
                            //System.out.println(usn);
                            for (String s : listaLeiloes){
                                out.println(s);
                                //System.out.println(s);
                            }
                            out.println("end");
                            break;

                        case 2:
                            if((curr = in.readLine()) != null);
                                idLeilao = Integer.parseInt(curr);
                                
                            out.println(myLeiloeira.precoLeilao(idLeilao));
                                
                            if((curr = in.readLine()) != null);
                                preco = Double.parseDouble(curr);
                                
                            if(myLeiloeira.Licitar(idLeilao, usn, preco)){
                                out.println("sucesso");
                            }
                            else
                                out.println("insucesso");
                            break;
                        default:
                            break;
                    }    
                }
            }
        }

        catch (IOException e){};
    }

}
