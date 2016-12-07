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
        password = pwd
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
        password = pwd
    }

    public boolean authenticate(String usn, String pwd){
        return (username.equals(usn) && password.equals(pwd));
    }
}


class Leiloeira{
    ArrayList<Leilao> leiloes;
    HashSet<Vendedor> vendedores;
    HashSet<Comprador> compradores;

    public Leiloeira(){
        leiloes = new ArrayList<>();
        vendedores = new TreeSet<>();
        compradores = new TreeSet<>();
    }

    public synchronized void addVendedor(String usn, String pwd){
        Vendedor novo = new Vendedor(usn,pwd);
        vendedores.add(novo);
    }

    public synchronized void addComprador(String usn, String pwd){
        Comprador novo = new Comprador(usn,pwd);
        compradores.add(novo);
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
            //Perguntar ao cliente se quer fazer login como vendedor ou comprador
            //Fazer coisas diferentes consoante a resposta
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
