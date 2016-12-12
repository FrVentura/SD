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
    
    @Override
    public String getUsername(){
        return username;
    }
    
    @Override
    public String getPassword(){
        return password;
    }
    
    public Vendedor(String usn, String pwd){
        username = usn;
        password = pwd;
    }
    
    public boolean authenticate(String usn, String pwd){
        return (username.equals(usn) && password.equals(pwd));
    }
}
