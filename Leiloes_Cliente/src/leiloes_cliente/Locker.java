/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_cliente;

import java.util.ArrayList;
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
    private ArrayList<String> arrList;
    
    public Locker(){
        l = new ReentrantLock();
        okGo = l.newCondition();
        available = false;
        arrList = new ArrayList<>();
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

    public ArrayList<String> getArrList() {
        return arrList;
    }

    public void setArrList(ArrayList<String> arrList) {
        this.arrList = arrList;
    }
    
    public ArrayList<String> getArrListDeep(){
        ArrayList<String> ret = new ArrayList();
        for (String s: arrList){
            ret.add(s);
        }
        return ret;
    }
    
    
}
