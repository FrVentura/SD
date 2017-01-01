package leiloes_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
                    menuComprador(locker);
                    break;
                case VENDEDOR:
                    menuVendedor(locker);
                    break; 
            }
        }while(modo!=Modo.QUIT);
        

        
        in.close();
        sin.close();
        out.close();
        cs.close();
        listen.interrupt();
        System.exit(0);
        
    }
    
    public static void menuComprador(Locker l) throws IOException, InterruptedException {
        do {
            compradorMenu.executa();
            String opcao = compradorMenu.getOpcao();
            
            switch(opcao){
                case "0":   
                    modo = Modo.QUIT;
                    out.println(opcao);
                    break;
                    
                case "1":
                    out.println(opcao);
                    l.getL().lock();
                    ArrayList<String> tmp = l.getArrListDeep();
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    System.out.println("\n\n:: Lista de Leilões ::\n");
                    for (String s : tmp)
                        System.out.println(s);
                    System.out.println("\n:: FIM ::");
                    break;
 
                case "2":
                    System.out.println("\n\n:: Licitar ::");
                    System.out.println("\nID do Leilão:");
                    
                    String input = sin.readLine();
                    
                    if(input.isEmpty()){
                        System.out.println("\nFormato errado.");
                        break;
                    }
                    try{
                        int testIntIdLicitar = Integer.parseInt(input);
                    }
                    catch(NumberFormatException e){
                        System.out.println("\nFormato errado.");
                        break;
                    }
                    
                    out.println(opcao);
     
                    out.println(input);
                    
                    l.getL().lock();
                    
                    double maiorLicitacao = Double.parseDouble(l.getReceived());
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    
                    if(maiorLicitacao == -1){
                        System.out.println("\nImpossivel licitar no leilão.");
                        break;
                    }
                    System.out.println("\nMaior licitação até ao momento: "+maiorLicitacao);
                    
                    System.out.print("\nColoque a sua licitação: ");
                    
                    input = sin.readLine();
                    
                    out.println(input);
                    
                    l.getL().lock();
                    String tmp2 = l.getReceived();
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    
                    if (tmp2.equals("sucesso")){
                        System.out.println("\nLicitação efetuada com sucesso.");
                    }
                    else{
                        System.out.println("\nNão foi possível inserir licitação.");
                    }
                    break;
                
            }
        } while (modo != Modo.QUIT);
    }
    
    public static void menuVendedor(Locker l) throws IOException {
        do {
            vendedorMenu.executa();
            String opcao = vendedorMenu.getOpcao();
            
            switch(opcao){
                case "0":
                    out.println(opcao);
                    modo = Modo.QUIT;
                    out.println(opcao);
                    break;
                    
                case "1":
                    out.println(opcao);
                    l.getL().lock();
                    ArrayList<String> tmp = l.getArrListDeep();
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    
                    System.out.println("\n\n:: Lista de Leilões ::\n");
                    for (String s : tmp)
                        System.out.println(s);
                    System.out.println("\n:: FIM ::");
                    break;
                    
                case "2":
                    System.out.println("\n\n:: Novo leilão ::");
                    System.out.print("\nItem a leiloar: ");
                    String item = sin.readLine();
                    if(item.isEmpty()){
                        System.out.println("\nFormato errado.");
                        break;
                    }
                    System.out.print("\nIntroduza o valor inicial: ");
                    String strValor = sin.readLine();
                    try{
                        double valor = Double.parseDouble(strValor);
                    }
                    catch(NumberFormatException | NullPointerException e){
                        System.out.println("\nFormato errado.");
                        break;
                    }
                    out.println(opcao);
                    out.println(item);
                    out.println(strValor);
                    l.getL().lock();
                    System.out.println("\nLeilao criado com sucesso com ID: "+l.getReceived());
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    
                    break;               
                   
                case "3":
                    System.out.print("\nIntroduza o ID do leilão que pretende terminar: ");
                    String strIdLeilaoTerminar = sin.readLine();
                    try{
                        int testIntIdTerminar = Integer.parseInt(strIdLeilaoTerminar);
                    }
                    catch(NumberFormatException e){
                        System.out.println("\nFormato errado.");
                        break;
                    }
                    
                    out.println(opcao);
                    out.println(strIdLeilaoTerminar);
                    l.getL().lock();
                    String strFechar = l.getReceived();
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    if(strFechar.equals("ErroFecharLeilao"))
                        System.out.println("\nErro ao fechar leilão.");
                    else{
                        System.out.println("\nLeilao terminado com sucesso. Id: "+strFechar);
                    }
  
                    break;
                    
                default:
                    break;

            }
        } while (modo != Modo.QUIT);
    }
    
    public static void menuRegistoLogin(Locker l) throws IOException, InterruptedException{
        String username;
        String password;

        mainMenu.executa();
        String opcao =  mainMenu.getOpcao();

        switch(opcao){
            case "0":
                    out.println(opcao);
                    modo = Modo.QUIT;
                    break;

            case "1":
                    out.println(opcao);
                    System.out.println("\n\n:: Registo de vendedor ::");
                    System.out.println("\nUsername:");
                    username = sin.readLine();
                    out.println(username);

                    System.out.println("\nPassword:");
                    password = sin.readLine();
                    out.println(password);
                    l.getL().lock();
                    if (l.getReceived().equals("situation1")){
                        modo = Modo.VENDEDOR;
                    }
                    else
                        System.out.println("\nDados inválidos.");
                    l.getOkGo().signalAll();
                    l.getL().unlock();

                    break;
                    
            case "2":
                    out.println(opcao);
                    System.out.println("\n\n:: Registo de comprador ::");
                    System.out.println("\nUsername:");
                    username = sin.readLine();
                    out.println(username);


                    System.out.println("\nPassword:");
                    password = sin.readLine();
                    out.println(password);
                    l.getL().lock();
                    if(l.getReceived().equals("situation2"))
                        modo = Modo.COMPRADOR;
                    else
                        System.out.println("\nDados inválidos");
                    l.getOkGo().signalAll();
                    l.getL().unlock();

                    break;
                    
            case "3":
                    out.println(opcao);
                    System.out.println("\n\n:: Login vendedor ::");
                    System.out.println("\nUsername:");
                    username = sin.readLine();
                    out.println(username);

                    System.out.println("\nPassword");
                    password = sin.readLine();
                    out.println(password);
                    l.getL().lock();
                    if(l.getReceived().equals("situation3"))
                        modo = Modo.VENDEDOR;
                    else
                        System.out.println("\nDados inválidos.");
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    
                    break;
                    
            case "4":
                    out.println(opcao);
                    System.out.println("\n\n:: Login comprador ::");
                    System.out.println("\nUsername:");
                    username = sin.readLine();
                    out.println(username);

                    System.out.println("\nPassword:");
                    password = sin.readLine();
                    out.println(password);
                    l.getL().lock();
                    if(l.getReceived().equals("situation4"))
                        modo = Modo.COMPRADOR;
                    else
                        System.out.println("\nDados inválidos.");
                    l.getOkGo().signalAll();
                    l.getL().unlock();
                    
                    break;
                
            default:
                break;
        }
    }
            
    public static void loadMenus(){
        String[] mainList = {"Registar como novo vendedor",
                             "Registar como novo comprador",
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
}

