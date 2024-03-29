/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.io.Serializable;

/**
 *
 * @author FrVentura
 */
public abstract class Utilizador implements Comparable, Serializable {
    
    String username;
    String password;

    public Utilizador(String usn, String pwd){
        username = usn;
        password = pwd;
    }
    
    public Utilizador(Utilizador u){
        username = u.getUsername();
        password = u.getPassword();
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public abstract boolean equals(Object o);
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(this.username);
        s.append(" ");
        s.append(this.password);
        return s.toString();
    }
    
    public boolean authenticate(String usn, String pwd){
        return (usn.equals(username) && pwd.equals(password));
    }
    

}
