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
public class Vendedor extends Utilizador{
    
    private String username;
    private String password;
    
    
    public Vendedor(String usn, String pwd){
        super(usn,pwd);
    }
    
    public Vendedor(Vendedor v){
        super(v.getUsername(),v.getPassword());
    }
    
    @Override
    public Vendedor clone(){
        return new Vendedor(this);
    }
}
