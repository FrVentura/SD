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
    boolean state;
    Thread t ;
    HandlerAssynchronous hand;

    public Handler(Socket cs, Leiloeira lei) throws IOException{
        mySocket = cs;
        out = new PrintWriter (cs.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        myLeiloeira = lei;
        state = true;
        t = null;
        hand = null;
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
                
            curr = in.readLine();
            choice = Integer.parseInt(curr);

            switch (choice){
                case 1:
                    usn = in.readLine();
                    pwd = in.readLine();
                    vend = new Vendedor(usn,pwd);
                    if (myLeiloeira.addUtilizador(vend) == true){
                        out.println("situation1");
                        situation = 1;
                       hand = new HandlerAssynchronous(mySocket,myLeiloeira,usn);
                    t =  (new Thread (hand));
                    t.start();
                    }
                    else{
                        out.println("from server: Fail");
                        situation = -1;
                    }
                    
                    break;
                    
                case 2:
                        usn = in.readLine();
                        pwd = in.readLine();
                        comp = new Comprador(usn,pwd);
                        if (myLeiloeira.addUtilizador(comp) == true){
                            out.println("situation2");
                            situation = 2;
                           hand = new HandlerAssynchronous(mySocket,myLeiloeira,usn);
                        t =  (new Thread (hand));
                        t.start();
                        }
                        else{
                            out.println("from server: Fail");
                            situation = -1;
                        }
                        break;
                case 3:
                    usn = in.readLine();
                    pwd = in.readLine();
                    if (myLeiloeira.authenticate(usn,pwd,1) == true){
                        out.println("situation3");
                        situation = 3;
                        hand = new HandlerAssynchronous(mySocket,myLeiloeira,usn);
                        t =  (new Thread (hand));
                        t.start();
                    }
                    else{
                        out.println("from server: Fail");
                        situation = -1;
                    }
                   
                    break;
                    
                case 4:
                    usn = in.readLine();
                    pwd = in.readLine();
                    if (myLeiloeira.authenticate(usn,pwd,2) == true){
                        out.println("situation4");
                        situation = 4;
                        hand = new HandlerAssynchronous(mySocket,myLeiloeira,usn);
                        t =  (new Thread (hand));
                        t.start();
                    }
                    else{
                        out.println("from server: Fail");
                        situation = -1;
                    }
                    
                    break;
                    
                case 0:
                    in.close();
                    out.close();
                    mySocket.close();
                    synchronized(myLeiloeira){
                        myLeiloeira.guardarLeiloeira();
                    }
                    return;

                default:
                    break;
            }
            
            
            // declaracao de variaveis auxiliares
            choice = -1;
            int idLeilao = -1;
            double preco = -1;
            String item;
                    
            if (situation==1 || situation==3){ // caso do Vendedor :: nao sai daqui ate desconectar
                
                while (state){
                    curr = in.readLine();
                    choice = Integer.parseInt(curr);

                    switch (choice){
                        case 1:
                            ArrayList<String> listaLeiloes = new ArrayList<>();
                            listaLeiloes = myLeiloeira.ListarLeiloes(usn);
                            out.println("from server: a Listar leiloes");
                            for (String s : listaLeiloes){
                                out.println(s);
                            }
                            out.println("end");
                            break;

                        case 2:
                            item = in.readLine();
                            curr = in.readLine();
                            preco = Double.parseDouble(curr);
                            idLeilao = myLeiloeira.addLeilao(item, usn, preco);
                            out.println(""+idLeilao);

                            break;

                        case 3:
                            curr = in.readLine();
                            idLeilao = Integer.parseInt(curr);
                            if (myLeiloeira.fecharLeilao(idLeilao, usn)){
                                out.println(idLeilao);
                            }
                            else{
                                out.println("ErroFecharLeilao");
                            }
                            break;
                            
                        case 0:
                            state = false;
                            in.close();
                            out.close();
                            mySocket.close();
                            synchronized(myLeiloeira){
                                myLeiloeira.guardarLeiloeira();
                            }
                            break;
                            
                        default:
                            break;
                    }    
                }
            }
            else if (situation == 2 || situation == 4){
                
                while (state){
                    
                    curr = in.readLine();
                    choice = Integer.parseInt(curr);

                    switch (choice){
                        case 1:
                            ArrayList<String> listaLeiloes = new ArrayList<>();
                            listaLeiloes = myLeiloeira.ListarLeiloes(usn);
                            out.println("from server: a Listar leiloes");
                            for (String s : listaLeiloes){
                                out.println(s);
                            }
                            out.println("end");
                            break;

                        case 2:
                            curr = in.readLine();
                            idLeilao = Integer.parseInt(curr);
                            
                            double precoLeilaoLicitar = myLeiloeira.precoLeilao(idLeilao);
                            if(precoLeilaoLicitar == -1){
                                out.println(precoLeilaoLicitar);
                                break;
                            }
                            
                            out.println(precoLeilaoLicitar);
                            
                            curr = in.readLine();
                            
                            try{
                                preco = Double.parseDouble(curr);
                            }
                            catch(NumberFormatException | NullPointerException e){
                                out.println("ErroLicitarLeilao");
                                break;
                            }
                                
                            if(myLeiloeira.Licitar(idLeilao, usn, preco)){
                                out.println("sucesso");
                            }
                            else
                                out.println("insucesso");
                            break;
                            
                        case 0:
                            state = false;
                            in.close();
                            out.close();
                            mySocket.close();
                            synchronized(myLeiloeira){
                                myLeiloeira.guardarLeiloeira();
                            }
                            break;
                            
                        default:
                            break;
                    }    
                }
            }
        }

        catch (IOException e){};
        
        hand.changeState();    
    try{
        this.t.join();

    }catch(InterruptedException e){}
        
        
    }

}
