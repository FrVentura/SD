import java.net.*;
import java.io.*;


public class clientSide{
    public static void main (String args[]) throws IOException, UnknownHostException{
        
        Socket cs = new Socket("127.0.0.1",9999);


        PrintWriter out = new PrintWriter (cs.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

        String current;
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

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
