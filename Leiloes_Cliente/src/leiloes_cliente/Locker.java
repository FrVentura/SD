/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_cliente;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author FrVentura
 */
public class Locker {
    private String received;
    private Lock l;
    private Condition okGo;
    private boolean available;
    
    public Locker(){
        l = new ReentrantLock();
        okGo = l.newCondition();
        available = false;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public Lock getL() {
        return l;
    }

    public void setL(Lock l) {
        this.l = l;
    }

    public Condition getOkGo() {
        return okGo;
    }

    public void setOkGo(Condition okGo) {
        this.okGo = okGo;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
   
    
    
    
    
}
