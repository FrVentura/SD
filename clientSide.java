import java.net.*;
import java.io.*;


class HandlerListener implements Runnable{
    Socket mySocket;
    BufferedReader in;

    public HandlerListener(Socket s, BufferedReader br){
        mySocket = s;
        in = br;
    }

    public void run(){
        while (true){
            try{
                System.out.println(in.readLine());
            }
            catch (IOException e){};
        }
    }
}


public class clientSide{
    public static void main (String args[]) throws IOException, UnknownHostException{
        
        Socket cs = new Socket("127.0.0.1", 9999);


        PrintWriter out = new PrintWriter (cs.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));


        String username;
        String password;
        String current;
        int choice;


        (new Thread (new HandlerListener(cs,in))).start();

        System.out.println("Prima 1 para se registar como novo vendedor");
        System.out.println("Prima 2 para se registar como novo comprador");
        System.out.println("Prima 3 para fazer login como vendedor");
        System.out.println("Prima 4 para fazer login como comprador");
        System.out.println("Digite \"end\" para sair");
        System.out.println("---------------------------------------------");
        current = sin.readLine();
        if (current.equals("end")){
            cs.shutdownOutput();
            return;
        }
        choice = Integer.parseInt(current);
        out.println(choice);
        switch (choice){
                    case 1:
                            //System.out.println(in.readLine());
                            System.out.println("Introduza um username");
                            username = sin.readLine();
                            out.println(username);

                            System.out.println("Introduza uma password");
                            password = sin.readLine();
                            out.println(password);
                            //System.out.println(in.readLine());
                            break;
                    case 2:
                            //System.out.println(in.readLine());
                            System.out.println("Introduza um username");
                            username = sin.readLine();
                            out.println(username);


                            System.out.println("Introduza uma password");
                            password = sin.readLine();
                            out.println(password);
                            //System.out.println(in.readLine());
                            break;
                    case 3:
                            //System.out.println(in.readLine());
                            System.out.println("Introduza o seu username");
                            username = sin.readLine();
                            out.println(username);

                            System.out.println("Introduza a sua password");
                            password = sin.readLine();
                            out.println(password);
                            //System.out.println(in.readLine());
                            break;
                    case 4:
                            //System.out.println(in.readLine());
                            System.out.println("Introduza o seu username");
                            username = sin.readLine();
                            out.println(username);

                            System.out.println("Introduza a sua password");
                            password = sin.readLine();
                            out.println(password);
                            //System.out.println(in.readLine());
                            break;
                    default:
                            break;
        }



        while((current = sin.readLine()) != null){
            if (current.equals("end")){
                cs.shutdownOutput();
                break;
            }

            out.println(current);
            System.out.println("got: " + in.readLine());
        }
        System.out.println("got: " + in.readLine());
        in.close();
        sin.close();
        out.close();
        cs.close();
        
    }
}
