package leiloes_cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Contém os métodos necessários para a utilização dos menus
 *
 * @author jreis
 */

public class Menu {
    // variáveis de instância
    private List<String> opcoes;
    private String op;
    BufferedReader sin;
   
    /**
     * Constructor for objects of class Menu
     */
    public Menu(String[] opcoes, BufferedReader sin) {
        this.opcoes = new ArrayList<String>();
        for (String op : opcoes)
            this.opcoes.add(op);
        this.op = "0";
        this.sin = sin;
    }
 
    /**
     * Método para apresentar o menu e ler uma opção.
     */
    public void executa() {
        do {
            showMenu();
            try {
                this.op = lerOpcao();
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while ("-1".equals(this.op));
    }
   
    /**
     * Apresentar o menu
     */
    private void showMenu() {
        System.out.println("\n :: Opções:");
        for (int i=0; i<this.opcoes.size(); i++) {
            System.out.print("[");
            System.out.print(i+1);
            System.out.print("] ");
            System.out.println(this.opcoes.get(i));
        }
        System.out.println("[0] Sair");
    }
   
    /**
     * Ler uma opção válida
     */
    private String lerOpcao() throws IOException {
        String op;
       
        System.out.print("Seleção: ");
        op = sin.readLine();
       
        if (Integer.parseInt(op)<0 || Integer.parseInt(op)>this.opcoes.size()) {
            System.out.println("Opção Inválida. Tente novamente.");
            op = "-1";
        }
        return op;
    }
   
    /**
     * Método para obter a última opção lida
     */
    public String getOpcao() {
        return this.op;
    }
    
    
}
