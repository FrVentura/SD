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
    
    public Vendedor(String usn, String pwd){
        super(usn,pwd);
    }
    
    public Vendedor(Vendedor v){
        super(v.getUsername(),v.getPassword());
    }
    
    @Override
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Vendedor v = (Vendedor) o;
        return this.username.equals(v.getUsername()) &&
               this.password.equals(v.getPassword());
    }
    
    @Override
    public Vendedor clone(){
        return new Vendedor(this);
    }
    
        @Override
    public int compareTo(Object o) {
        Vendedor c = (Vendedor) o;
        return (this.username.compareTo(c.username));
    }
}
