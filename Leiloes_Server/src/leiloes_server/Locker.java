/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author FrVentura
 */
public class Locker {
    private final Lock lInc;
    private final Condition okToReadInc;
    private final Condition okToWriteInc;
    private int numReInc;
    private int numWrInc;
    int wantWriteInc;
    
    private final Lock lAti;
    private final Condition okToReadAti;
    private final Condition okToWriteAti;
    private int numReAti;
    private int numWrAti;
    int wantWriteAti;
    
    private final Lock lHis;
    private final Condition okToReadHis;
    private final Condition okToWriteHis;
    private int numReHis;
    private int numWrHis;
    int wantWriteHis;
    
    private final Lock lUti;
    private final Condition okToReadUti;
    private final Condition okToWriteUti;
    private int numReUti;
    private int numWrUti;
    int wantWriteUti;
        
    public Locker(){
        lInc = new ReentrantLock();
        okToReadInc = lInc.newCondition();
        okToWriteInc = lInc.newCondition();
        numReInc = 0;
        numWrInc = 0;
        wantWriteInc = 0;

        lAti = new ReentrantLock();
        okToReadAti = lAti.newCondition();
        okToWriteAti = lAti.newCondition();
        numReAti = 0;
        numWrAti = 0;
        wantWriteInc = 0;

        
        lHis = new ReentrantLock();
        okToReadHis = lHis.newCondition();
        okToWriteHis = lHis.newCondition();
        numReHis = 0;
        numWrHis = 0;
        wantWriteInc = 0;

        
        lUti = new ReentrantLock();
        okToReadUti = lUti.newCondition();
        okToWriteUti = lUti.newCondition();
        numReUti = 0;
        numWrUti = 0; 
        wantWriteUti = 0;

    }
    
     public void readLockInc(){
        lInc.lock();
        while (numReInc > 0 || numWrInc > 0){
            try{
                okToReadInc.await();
            }
            catch (InterruptedException e){};
        }
        numReInc++;
        okToReadInc.signalAll();
        lInc.unlock();
    }
     
     public void readLockAti(){
        lAti.lock();
        while (numReAti > 0 || numWrAti > 0){
            try{
                okToReadAti.await();
            }
            catch (InterruptedException e){};
        }
        numReAti++;
        okToReadAti.signalAll();
        lInc.unlock();
    }
     
     public void readLockHis(){
        lHis.lock();
        while (numReHis > 0 || numWrHis > 0){
            try{
                okToReadHis.await();
            }
            catch (InterruptedException e){};
        }
        numReHis++;
        okToReadHis.signalAll();
        lHis.unlock();
    }

    public void readLockUti(){
        lUti.lock();
        while (numReUti > 0 || numWrUti > 0){
            try{
                okToReadUti.await();
            }
            catch (InterruptedException e){};
        }
        numReUti++;
        okToReadUti.signalAll();
        lUti.unlock();
    }

    public void readUnlockInc(){
        lInc.lock();
        numReInc--;
        if (numReInc==0)
            okToWriteInc.signalAll();
    }

    public void readUnlockAti(){
        lAti.lock();
        numReAti--;
        if (numReAti==0)
            okToWriteAti.signalAll();
    }
 
    public void readUnlockHis(){
        lHis.lock();
        numReHis--;
        if (numReHis==0)
            okToWriteHis.signalAll();
    }
    
    public void readUnlockUti(){
        lUti.lock();
        numReUti--;
        if (numReUti==0)
            okToWriteUti.signalAll();
    }
    
    public void writeLockInc(){
        lInc.lock();
        wantWriteInc++;
        while (numWrInc > 0 || numReInc > 0){
            try{
                okToWriteInc.await();
            }
            catch (InterruptedException e){};
        }
        wantWriteInc--;
        numWrInc++;
        lInc.unlock();
    }
    
    public void writeLockAti(){
        lAti.lock();
        wantWriteAti++;
        while (numWrAti > 0 || numReAti > 0){
            try{
                okToWriteAti.await();
            }
            catch (InterruptedException e){};
        }
        wantWriteAti--;
        numWrAti++;
        lAti.unlock();
    }
    
    
    public void writeLockHis(){
        lHis.lock();
        wantWriteHis++;
        while (numWrHis > 0 || numReHis > 0){
            try{
                okToWriteHis.await();
            }
            catch (InterruptedException e){};
        }
        wantWriteHis--;
        numWrHis++;
        lHis.unlock();
    }
    
    public void writeLockUti(){
        lUti.lock();
        wantWriteUti++;
        while (numWrUti > 0 || numReUti > 0){
            try{
                okToWriteUti.await();
            }
            catch (InterruptedException e){};
        }
        wantWriteUti--;
        numWrUti++;
        lUti.unlock();
    }
    
    public void writeUnlockInc(){
        lInc.lock();
        numWrInc--;
        okToReadInc.signalAll();
        okToWriteInc.signalAll();
        lInc.unlock();
    }
    
    
    public void writeUnlockAti(){
        lAti.lock();
        numWrAti--;
        okToReadAti.signalAll();
        okToWriteAti.signalAll();
        lAti.unlock();
    }
    
    public void writeUnlockHis(){
        lHis.lock();
        numWrHis--;
        okToReadHis.signalAll();
        okToWriteHis.signalAll();
        lHis.unlock();
    }
    
    
    public void writeUnlockUti(){
        lUti.lock();
        numWrUti--;
        okToReadUti.signalAll();
        okToWriteUti.signalAll();
        lUti.unlock();
    }
}
