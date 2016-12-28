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
    
    public static void main (String args[]) throws IOException, UnknownHostException, InterruptedException{
        
        Socket cs = new Socket("127.0.0.1", 9999);
        
        

        Locker locker = new Locker();
        out = new PrintWriter (cs.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        sin = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("passei");

        loadMenus();
        
        Thread listen = new Thread (new HandlerListener(cs,in,locker));
        listen.start();
        modo = Modo.VISITOR;
        
        do {
            switch(modo){
                case VISITOR:
                    menuRegistoLogin(locker);
                    break;
                case COMPRADOR:
                    menuComprador();
                    break;
                case VENDEDOR:
                    menuVendedor(locker);
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
    
    public static void menuVendedor(Locker l) throws IOException {
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
                    l.getL().lock();
                    System.out.println("Leilao criado com sucesso. Id: "+l.getReceived());
                    l.getL().unlock();
                    l.setAvailable(true);
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
    
    public static void menuRegistoLogin(Locker l) throws IOException, InterruptedException{
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
                        reler = false;
                        modo = Modo.QUIT;
                        break;
                        
                case "1":
                        reler = false;
                        //System.out.println(in.readLine());
                        System.out.println("Introduza um username");
                        username = sin.readLine();
                        out.println(username);

                        System.out.println("Introduza uma password");
                        password = sin.readLine();
                        out.println(password);
                        l.getL().lock();
                        //l.getOkGo().await();
                        if (l.getReceived().equals("situation1")){
                            modo = Modo.VENDEDOR;
                        }
                        else
                            System.out.println("Dados inválidos");
                        l.getL().unlock();
                        l.setAvailable(true);
                        
                        break;
                case "2":
                        reler = false;
                        //System.out.println(in.readLine());
                        System.out.println("Introduza um username");
                        username = sin.readLine();
                        out.println(username);


                        System.out.println("Introduza uma password");
                        password = sin.readLine();
                        out.println(password);
                        l.getL().lock();
                        if(l.getReceived().equals("situation2"))
                            modo = Modo.COMPRADOR;
                        else
                            System.out.println("Dados inválidos");
                        l.getL().unlock();
                        l.setAvailable(true);
                        
                        break;
                case "3":
                        reler = false;
                        //System.out.println(in.readLine());
                        System.out.println("Introduza o seu username");
                        username = sin.readLine();
                        out.println(username);

                        System.out.println("Introduza a sua password");
                        password = sin.readLine();
                        out.println(password);
                        l.getL().lock();
                        if(l.getReceived().equals("situation3"))
                            modo = Modo.COMPRADOR;
                        else
                            System.out.println("Dados inválidos");
                        l.getL().unlock();
                        l.setAvailable(true);
                        break;
                case "4":
                        reler = false;
                        //System.out.println(in.readLine());
                        System.out.println("Introduza o seu username");
                        username = sin.readLine();
                        out.println(username);

                        System.out.println("Introduza a sua password");
                        password = sin.readLine();
                        out.println(password);
                        l.getL().lock();
                        if(l.getReceived().equals("situation4"))
                            modo = Modo.COMPRADOR;
                        else
                            System.out.println("Dados inválidos");
                        l.getL().unlock();
                        l.setAvailable(true);
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

