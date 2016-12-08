import java.io.*;
import java.net.*;
import java.util.*;

class Leilao{
    int numero;
    String vendedor;
    String ultLicitador;
    int valor;
    String item;

    public Leilao(int num, String ven, int val, String it){
        numero = num;
        vendedor = ven;
        ultLicitador = "none";
        valor = val;
        item = it;
    }

    public void licitar(String comprador, int incremento){
        ultLicitador = comprador;
        valor += incremento;
    }

}

class Vendedor{
    String username;
    String password;

    public Vendedor(String usn, String pwd){
        username = usn;
        password = pwd;
    }

    public boolean authenticate(String usn, String pwd){
        return (username.equals(usn) && password.equals(pwd));
    }
}

class Comprador{
    String username;
    String password;

    public Comprador(String usn, String pwd){
        username = usn;
        password = pwd;
    }

    public boolean authenticate(String usn, String pwd){
        return (username.equals(usn) && password.equals(pwd));
    }
}


class Leiloeira{
    ArrayList<Leilao> leiloes;
    TreeMap<String,String> vendedores;
    TreeMap<String,String> compradores;

    public Leiloeira(){
        leiloes = new ArrayList<>();
        vendedores = new TreeMap<>();
        compradores = new TreeMap<>();
    }

    public synchronized boolean addVendedor(String usn, String pwd){
        if (vendedores.containsKey(usn))
            return false;
        vendedores.put(usn,pwd);
        return true;
    }

    public synchronized boolean addComprador(String usn, String pwd){
        if (compradores.containsKey(usn))
            return false;
        compradores.put(usn,pwd);
        return true;
    }

    public boolean authenticateVend(String usn, String pwd){
        if (vendedores.containsKey(usn) && vendedores.get(usn).equals(pwd))
            return true;
        return false;
    }

    public boolean authenticateComp(String usn, String pwd){
        if (compradores.containsKey(usn) && compradores.get(usn).equals(pwd))
            return true;
        return false;
    }

    public Set<String> listarVendedores(){
        return vendedores.keySet();
    }

    public Set<String> listarCompradores(){
        return compradores.keySet();
    }

}

class Handler implements Runnable{
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
            String usn;
            String pwd;
            int choice;
            try{
                if( (curr = in.readLine()) != null);
                    choice = Integer.parseInt(curr);

                switch (choice){
                    case 1:
                            out.println("from server: a registar novo vendedor");
                            usn = in.readLine();
                            pwd = in.readLine();
                            if (myLeiloeira.addVendedor(usn,pwd) == true)
                                out.println("from server: Success, username is \""+ usn + "\" password is \""+pwd+"\"");
                            else
                                out.println("from server: Fail");
                    case 2:
                            out.println("from server: a registar novo comprador");
                            usn = in.readLine();
                            pwd = in.readLine();
                            if (myLeiloeira.addComprador(usn,pwd) == true)
                                out.println("from server: Success, username is \""+ usn + "\" password is \""+pwd+"\"");
                            else
                                out.println("from server: Fail");
                    case 3:
                            out.println("from server: a fazer login como vendedor");
                            usn = in.readLine();
                            pwd = in.readLine();
                            if (myLeiloeira.authenticateVend(usn,pwd) == true)
                                out.println("from server: bem-vindo");
                            else
                                out.println("from server: Fail");
                    case 4:
                            out.println("from server: a fazer login como compradore");
                            usn = in.readLine();
                            pwd = in.readLine();
                            if (myLeiloeira.authenticateComp(usn,pwd) == true)
                                out.println("from server: bem-vindo");
                            else
                                out.println("from server: Fail");
                    default:
                        break;
                }
            }

                
            catch (IOException e){};
        }
}

public class serverSide{
    public static void main (String args[]) throws IOException{

        ServerSocket ss = new ServerSocket(9999);
        Socket cs = null;
        Leiloeira sothebys = new Leiloeira();

        while(true){
            cs = ss.accept();
            (new Thread (new Handler(cs,sothebys))).start();
        }
        
    }
}
