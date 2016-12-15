/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

/**
 *
 * @author FrVentura
 */
public class Comprador extends Utilizador{

    public Comprador(String usn, String pwd){
        super(usn,pwd);
    }
    
    public Comprador(Comprador c){
        super(c.getUsername(),c.getPassword());
    }
    
    @Override
    public Comprador clone(){
        return new Comprador(this);
    }

}
