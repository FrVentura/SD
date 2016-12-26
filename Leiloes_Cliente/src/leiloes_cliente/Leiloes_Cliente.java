package leiloes_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Leiloes_Cliente {
    private static PrintWriter out;
    private static BufferedReader in;
    private static BufferedReader sin;
    private static Menu mainMenu, vendedorMenu, compradorMenu;
    private static Modo modo;
    
    
    public enum Modo {
        QUIT, VISITOR, COMPRADOR, VENDEDOR
    }
    
    public static void main (String args[]) throws IOException, UnknownHostException{
        
        Socket cs = new Socket("127.0.0.1", 9999);

        out = new PrintWriter (cs.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        sin = new BufferedReader(new InputStreamReader(System.in));

        loadMenus();        
        (new Thread (new HandlerListener(cs,in))).start();
        modo = Modo.VISITOR;
        
        do {
            System.out.println("ola");
            switch(modo){
                case VISITOR:
                    menuRegistoLogin();
                    break;
                case COMPRADOR:
                    menuComprador();
                    break;
                case VENDEDOR:
                    menuVendedor();
                    break;                               
            }
        }while(modo!=Modo.QUIT);

        //in.close();
        //sin.close();
        //out.close();
        //cs.close();
        
    }
    
    public static void menuComprador() throws IOException {
        do {
            clean();
            compradorMenu.executa();
            String opcao = compradorMenu.getOpcao();
            out.println(opcao);
            
            switch(opcao){
                case "0":   
                    modo = Modo.QUIT;
                case "1":
                    clean();
                    System.out.println(in.readLine()); // COMO É Q VOU RECEBER A LISTA?
 
                case "2":
                    clean();
                    System.out.print("ID do Leilão: ");
                    String input = sin.readLine();
                    out.println(input);
                    
                    System.out.print("A maior licitação é: " + in.readLine() + ".\nLicitação: ");
                    input = sin.readLine();
                    out.println(input);
            }
        } while (modo != Modo.QUIT);
    }
    
    public static void menuVendedor() throws IOException {
        do {
            clean();
            vendedorMenu.executa();
            String opcao = vendedorMenu.getOpcao();
            out.println(opcao);
            
            switch(opcao){
                case "0":
                    modo = Modo.QUIT;
                    break;
                    
                case "1":
                    clean();
                    System.out.println(in.readLine());
                    break;
                    
                case "2":
                    String input;
                    System.out.print("Introduza o item que pretende leiloar: ");
                    input = sin.readLine();
                    out.println(input);
                    
                    System.out.print("Introduza o valor inicial: ");
                    input = sin.readLine();
                    out.println(input);
                    break;
                
                case "3":
                    String input2;
                    System.out.print("Introduza o ID do leilão que pretende terminar: ");
                    input2 = sin.readLine();
                    out.println(input2);
                    break;
                    
                    
            }
        } while (modo != Modo.QUIT);
    }
    
    public static void menuRegistoLogin() throws IOException{
        String username;
        String password;
        boolean reler = false;     

        do{
            clean();
            mainMenu.executa();
            String opcao =  mainMenu.getOpcao();
            out.println(opcao);
            
            switch(opcao){
                case "0":
                        modo = Modo.QUIT;
                        
                case "1":
                        //System.out.println(in.readLine());
                        System.out.println("Introduza um username");
                        username = sin.readLine();
                        out.println(username);

                        System.out.println("Introduza uma password");
                        password = sin.readLine();
                        out.println(password);
                        modo = Modo.VENDEDOR;
                        //System.out.println(in.readLine());
                        break;
                case "2":
                        //System.out.println(in.readLine());
                        System.out.println("Introduza um username");
                        username = sin.readLine();
                        out.println(username);


                        System.out.println("Introduza uma password");
                        password = sin.readLine();
                        out.println(password);
                        modo = Modo.COMPRADOR; // FALTA MUDAR PARA SABER SE LOGIN E VALIDO OU N
                        //System.out.println(in.readLine());
                        break;
                case "3":
                        //System.out.println(in.readLine());
                        System.out.println("Introduza o seu username");
                        username = sin.readLine();
                        out.println(username);

                        System.out.println("Introduza a sua password");
                        password = sin.readLine();
                        out.println(password);
                        modo = Modo.VENDEDOR; // FALTA MUDAR PARA SABER SE LOGIN E VALIDO OU N
                        //System.out.println(in.readLine());
                        break;
                case "4":
                        //System.out.println(in.readLine());
                        System.out.println("Introduza o seu username");
                        username = sin.readLine();
                        out.println(username);

                        System.out.println("Introduza a sua password");
                        password = sin.readLine();
                        out.println(password);
                        modo = Modo.COMPRADOR; // FALTA MUDAR PARA SABER SE LOGIN E VALIDO OU N
                        //System.out.println(in.readLine());
                        break;
                default:
                        reler = true;
                        break;
            }
        } while (reler);
    }
            
    public static void loadMenus(){
        String[] mainList = {"Prima 1 para se registar como novo vendedor",
                           "Prima 2 para se registar como novo comprador",
                           "Login como vendedor",
                           "Login como comprador"
        };
        
        String[] vendedorList = {"Listar leiloes",
                                 "Iniciar novo leilao",
                                 "Finalizar um leilao"
        };
        
        String[] compradorList = {"Listar leiloes",
                                  "Licitar Leilão"
        };
        
        mainMenu = new Menu(mainList,sin);
        vendedorMenu = new Menu(vendedorList,sin);
        compradorMenu = new Menu(compradorList,sin);
        
        
    }
    
    public static void clean(){
        System.out.print('\u000C');
    }
    
    
        
        
}

