/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

/**
 *
 * @author brfc
 */
public class ObjState {
    
    
    
    private boolean state;
    
    
    public ObjState(boolean s){
    
    this.state = s;
    
    
    }
    
    
    public void csTfalse(){this.state = false;}
    public void csTtrue(){ this.state = true;}
    public boolean getState(){return this.state;}
    
    
    
    
}
