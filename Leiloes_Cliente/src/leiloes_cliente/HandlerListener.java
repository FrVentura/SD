package leiloes_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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


    @Override
    public void run(){
        
        try{
            
            String fromSv;
            while(true){
                
                locker.getL().lock();
                locker.setAvailable(false);
                fromSv = in.readLine();
                //System.out.println(fromSv);
                locker.setReceived(fromSv);
                                
                
                if (fromSv.equals("from server: a Listar leiloes")){
                    locker.getArrList().clear();
                    fromSv = in.readLine();
                    while (fromSv.equals("end")==false){
                        locker.getArrList().add(fromSv);
                        fromSv = in.readLine();
                    }
                }
                locker.getL().unlock();
                while (locker.isAvailable()==false);
            }
                
                

           }
        catch (IOException ex) {
            Logger.getLogger(HandlerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
