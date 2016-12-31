package leiloes_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandlerListener implements Runnable{
    
    Socket cs;
    BufferedReader in;
    Locker locker;

    public HandlerListener(Socket s, BufferedReader br, Locker l) throws IOException, UnknownHostException{
        in = br;
        cs = s;
        locker = l;
        
    }
    
    public void trataMensagemAsync(String in){
        String [] arr;
        arr = in.split(",");
        System.out.println("");
        System.out.println("/\\/\\/\\/\\ LEILAO FINALIZADO /\\/\\/\\/\\");
        for (String s : arr){
            System.out.println(s);
        }
        System.out.println("/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/");
        System.out.print("Seleção: ");
    }


    @Override
    public void run(){
        
        try{
            
            String fromSv;
            while(true){
                
                locker.getL().lock();
                locker.setAvailable(false);
                fromSv = in.readLine();
                
                while (fromSv.startsWith("async:")){
                    //System.out.println(fromSv.substring(6));
                    trataMensagemAsync(fromSv.substring(6));
                    fromSv = in.readLine();
                }

                if (fromSv.equals("from server: a Listar leiloes")){
                    System.out.println("passei");
                    ArrayList<String> tmp = new ArrayList<>();
                    fromSv = in.readLine();
                    while(fromSv.equals("end")==false){
                        tmp.add(fromSv);
                        fromSv = in.readLine();
                    }
                    locker.setArrList(tmp);
                }
                else{
                    locker.setReceived(fromSv);
                }
                locker.getL().unlock();
                
                ///// teste /////
                try {
                    locker.getL().lock();
                    locker.getOkGo().await();
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(HandlerListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
                

           }
        catch (IOException ex) {
            Logger.getLogger(HandlerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
