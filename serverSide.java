import java.io.*;
import java.net.*;
import java.util.*;

class Leilao{
    int numero;
    int vendedor;
    int ultLicitador;
    int valor;
    String item;

    public Leilao(int num, int ven, int val, String it){
        numero = num;
        vendedor = ven;
        ultLicitador = -1;
        valor = val;
        item = it;
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

    public synchronized void addVendedor(String usn, String pwd){
        vendedores.put(usn,pwd);
    }

    public synchronized void addComprador(String usn, String pwd){
        compradores.put(usn,pwd);
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
                out.println("Prima 1 para se registar como novo vendedor");
                out.println("Prima 2 para se registar como novo comprador");
                out.println("Prima 3 para fazer login como vendedor");
                out.println("Prima 4 para fazer login como comprador");
                out.println("---------------------------------------------");
                out.println("Digite \"end\" para sair");
                if ((curr = in.readLine()) == null){
                    out.println("Adeus");
                    return;
                }
                choice = Integer.parseInt(curr);
                switch (choice){
                    case 1:
                            out.println("Introduza um username");
                            usn = in.readLine();
                            out.println("Introduza uma password");
                            pwd = in.readLine();
                            if (usn != null && pwd != null){
                                myLeiloeira.addVendedor(usn,pwd);
                            }
                            if (myLeiloeira.authenticateVend(usn,pwd))
                                out.println("Vendedor Introduzido com sucesso");
                            break;
                    case 2:
                            out.println("Introduza um username");
                            usn = in.readLine();
                            out.println("Introduza uma password");
                            pwd = in.readLine();
                            if (usn != null && pwd != null){
                                myLeiloeira.addComprador(usn,pwd);
                            }
                            if (myLeiloeira.authenticateComp(usn,pwd))
                                out.println("Comprador Introduzido com sucesso");
                            break;
                    default:
                            return;

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
