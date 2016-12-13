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
public abstract class Utilizador {
    
    String username;
    String password;

    public Utilizador(String usn, String pwd){
        username = usn;
        password = pwd;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public boolean authenticate(String usn, String pwd){
        return (usn.equals(username) && pwd.equals(password));
    }
}
