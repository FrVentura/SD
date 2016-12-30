/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FrVentura
 */
public class InfoLeilaoFinalizado {
    
   
    private int id;
    private String vendedor;
    private String vencedor;
    private double valor;
    
    // nesta lista colocar o vendedor também.
    private List<String> licitadores;
    
    
    
    public InfoLeilaoFinalizado(int idLeilao, Leilao l){
        
        this.id = idLeilao;
        this.vendedor = l.getVendedor().getUsername();
        this.vencedor = l.getCompradorMaiorLance().getUsername();
        this.valor = l.getPreco();
        
        this.licitadores = new ArrayList();
                for(Comprador c : l.getListaLances()){
                
                    this.licitadores.add(c.getUsername());
                    
                
                }
                this.licitadores.add(this.vendedor);
                
                
        
        
        
        
        /*id = idLeilao;
        vendedor = l.getVendedor().getUsername();
        if (l.getCompradorMaiorLance()!=null)
            vencedor = l.getCompradorMaiorLance().getUsername();
            
        else
            vencedor = "sem vencedor";
        licitadores = new ArrayList<>();
        for (Comprador c : l.getListaLances()){
            licitadores.add(c.getUsername());*/
        
        }
        
        
        
    
    
    public synchronized StringBuilder getAviso(String user)
    {
        StringBuilder res = new StringBuilder(); 
       
            
           
        // caso a thread que requesita a inf seja a do vendedor
            if(user.equals(this.vendedor)){
                res.append("Leilao: " + id +"\n");
                res.append("Vendedor: " + vendedor + "\n");
                res.append("Vencedor: " + vencedor + "\n");
                res.append("Valor :" + valor +"\n");
                this.licitadores.remove(user);
                
            return res;
            }
            
        
          for(String usn : licitadores)
            if(usn.equals(user)){
                this.licitadores.remove(user);
                res.append("Leilao: " +id +"\n");
                res.append("Vendedor: " + vendedor + "\n");
                res.append("Vencedor: " + vencedor + "\n");
                res.append("Valor :" + valor +"\n");
                this.licitadores.remove(user);
                return res;
             }
         
          return res;
    
    
    }
    
    
    
    public synchronized boolean vazio(){
    
        return licitadores.isEmpty();
        
    }
    
    
    
    
}
