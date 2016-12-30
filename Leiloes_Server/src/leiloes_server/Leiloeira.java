/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Renato
 */
public class Leiloeira {

    private Integer incrementador;   
    private TreeMap<Integer,Leilao> ativos; // 
    private TreeMap<Integer,Leilao> historico; // tem todos os Leilões que já acabaram
    private TreeMap<String,Utilizador> utilizadores; // A string é o usn do utilizador.
    
    private ArrayList<InfoLeilaoFinalizado> aAvisar;
    private Locker locker;
    private int ultFechado;
    
    
    
    // temporárias -> a passar para a class Lock
    // invoca este lock quem mexe em aAvisar, ultFechado, método fecharLeilao e método esperarPorHistorico
    private final Lock l;
    Condition espera;



	

        // Construtores
    public Leiloeira(){
        this.incrementador = 0;
	this.ativos = new TreeMap<> ();
	this.historico = new TreeMap<> ();
	this.utilizadores = new TreeMap<> ();
        this.locker = new Locker();
        
        this.aAvisar = new ArrayList();
        // temporárias
        l = new ReentrantLock();
        espera = l.newCondition();
        
    }

    /**
     *
     * @param leil
     * Construtor copia : Devolve 'deep copy' de 
     */
    public Leiloeira(Leiloeira leil){  // deep copy
        this.incrementador = leil.getIncrementador();
	this.utilizadores = leil.getUtilizadoresDeep();
	this.ativos = leil.getAtivosDeep(); 
        this.historico = leil.getHistoricoDeep();
        this.locker = new Locker(); // vale a pena fazer deep?
        this.ultFechado = leil.getUtFechado();
        
        this.aAvisar = leil.getaAvisar();
        // temporarias
        this.l = new ReentrantLock();
        espera = l.newCondition();
    }

	// Gets
    public ArrayList<InfoLeilaoFinalizado> getaAvisar(){
    
        ArrayList<InfoLeilaoFinalizado> res = new ArrayList<InfoLeilaoFinalizado> ();
        
        for(InfoLeilaoFinalizado info : this.aAvisar)
            res.add(info);
        
        
        return res;
    
    }

    public int getIncrementador(){
	return this.incrementador;
    }
    
    public TreeMap<String,Utilizador> getUtilizadoresShallow(){
        return this.utilizadores;
    }
    
    public TreeMap<String,Utilizador> getUtilizadoresDeep(){
        Utilizador u;
        TreeMap <String,Utilizador> ret = new TreeMap<>();
        for(String usn : utilizadores.keySet()){
            u = utilizadores.get(usn);
            if (u instanceof Vendedor){
                ret.put(usn,((Vendedor) u).clone());
            }
            else{
                ret.put(usn,((Comprador) u).clone());
            }
        }
        return ret;
    }
    
    public TreeMap<Integer,Leilao> getAtivosShallow(){
        return ativos;
    }
 
    public TreeMap<Integer,Leilao> getAtivosDeep(){
        TreeMap<Integer,Leilao> ret = new TreeMap<>();
        for (Map.Entry<Integer,Leilao> entry : ativos.entrySet()){
            ret.put(entry.getKey(), entry.getValue().clone());
        }
        return ret;
    }
    
    public TreeMap<Integer,Leilao> getHistoricoShallow(){
        return historico;
    }
    
    public TreeMap<Integer,Leilao> getHistoricoDeep(){
        TreeMap<Integer,Leilao> ret = new TreeMap<>();
        for (Map.Entry<Integer,Leilao> entry : historico.entrySet()){
            ret.put(entry.getKey(), entry.getValue().clone());
        }
        return ret;
    }
    
    private int getUtFechado() {
        return ultFechado;
    }

    
    public int addLeilao(String item , String usn , double p){
        int aux;
        Vendedor vend;
        Utilizador u;
        locker.readLockUti();
        u = utilizadores.get(usn);
        locker.readUnlockUti();
        if (u instanceof Vendedor)
            vend = (Vendedor) u;
        else{
            return -1;
        }
        
        
        locker.writeLockInc();
        incrementador++;
        aux = incrementador;
        locker.writeUnlockInc();
        
        locker.writeLockAti();
        ativos.put(aux,(new Leilao(item,vend,p)));
        locker.writeUnlockAti();
        
	return aux;	
    }

    public boolean addUtilizador(Utilizador u){
        boolean ret;
        locker.writeLockUti();
        if(utilizadores.containsKey(u.getUsername()))
            ret = false;
	else{
            utilizadores.put(u.getUsername(),u);
            ret = true;
        }
        locker.writeUnlockUti();
        return ret;
    }
        
    
	// Modo 1 -> Vendedor
	// Modo 2 -> Comprador
    public boolean authenticate(String usn , String pw ,int modo){
        boolean ret = false;
        locker.readLockUti();
	if (this.utilizadores.containsKey(usn)){
            switch(modo){
                case 1: if((utilizadores.get(usn)) instanceof Vendedor && ((utilizadores.get(usn)).getPassword().equals(pw)))
                            ret = true;
                    break;
				
		case 2: if((utilizadores.get(usn)) instanceof Comprador && ((utilizadores.get(usn)).getPassword().equals(pw)))
                            ret = true;
                    break;
                    
		default:
                    break;
            }
        }
        locker.readUnlockUti();
        return ret;
    } 	

	

    public boolean Licitar (Integer idLeil, String usn, double valor){
        Leilao tmp;
        boolean ret = false;
        Utilizador u;
        locker.readLockUti();
        u = utilizadores.get(usn);
        locker.readUnlockUti();
        if (u == null)
            return false;
        
        locker.readLockAti();
        tmp = ativos.get(idLeil);
        if (tmp == null){
            locker.readUnlockAti();
            return false;
        }
        synchronized(tmp){
            locker.readUnlockAti();
            ret = tmp.licitar((Comprador) u, valor);
            //ret = true;
        }
        
        return ret;
    }
	
    public ArrayList<String> ListarLeiloes(String usn){
        ArrayList<String> ret = new ArrayList<>();
        Utilizador u;
        locker.readLockUti();
        u = utilizadores.get(usn);
        locker.readUnlockUti();
        if (u == null)
            return null;
        
        locker.readLockAti();
        if (u instanceof Vendedor){
            for (Map.Entry<Integer,Leilao> entry : ativos.entrySet()){
                StringBuilder s = new StringBuilder();
                s.append("Leilão nº ");
                s.append(entry.getKey());
                s.append("\n");
                s.append(entry.getValue().toStringV(usn));
                ret.add(s.toString());
            }
        }
        else{
            for (Map.Entry<Integer,Leilao> entry : ativos.entrySet()){
                StringBuilder s = new StringBuilder();
                s.append("Leilão nº ");
                s.append(entry.getKey());
                s.append("\n");
                s.append(entry.getValue().toStringC(usn));
                ret.add(s.toString());
            }
        }
        locker.readUnlockAti();
        return ret;
    }
	
    public synchronized boolean  fecharLeilao(Integer idLeil, String usn){
        
        // Adquirir locks
       locker.writeLockHis();
            locker.writeLockaAv();
                locker.writeLockUlF();
                
                try{
                // Processo
                //Remover dos ativos
                Leilao l = this.ativos.get(idLeil);
                this.ativos.remove(idLeil);
                
                //Colocar no historico
                this.historico.put(idLeil, l);
                InfoLeilaoFinalizado info = new InfoLeilaoFinalizado(idLeil,l);
                
                //Atualizar aAvisar
                this.aAvisar.add(info);
                
                //atualizar UltFechado
                this.ultFechado = idLeil;
                
                
                //enviar signall às threads que esperam por histórico
                 // ALTERAR ISTO   
                 notifyAll();   
                    
                 return true;
                 
               }catch(Exception e){ return false;  
               }
                // Libertar Locks
                finally{ 
                 locker.writeUnlockUlF();
            locker.writeUnlockAti();
          locker.writeUnlockHis();
                }
                
                
            
        
        /*Leilao l;
        locker.writeLockUti();
        if(ativos.containsKey(idLeil)){
            l = ativos.get(idLeil);
            if(l.getVendedor().getUsername().equals(usn))
                ativos.remove(idLeil);
            else{
                locker.writeUnlockAti();
                return false;
            }
        }
        else{
            locker.writeUnlockAti();
            return false;
        }
        
        locker.writeLockHis();
        locker.writeLockaAv();
        
        historico.put(idLeil,l);
        aAvisar.add(new InfoLeilaoFinalizado(idLeil,l));
        

        locker.writeUnlockaAv();
	locker.writeUnlockHis();
        
	return true; /*
        
        */
    }
    
    
    
    // mudar o lock para o mesmo que fecha os leiloes e arranjar isso
    public synchronized StringBuilder esperarPorHistorico(String usn) throws InterruptedException{
        
        boolean state = true;
        StringBuilder s = new StringBuilder();
        s.append("async:");
        
        
        
   
       while(state){
       
        
        // obter o lock para aAvisar escrever e ler
        locker.writeLockAti();
          
            
        if(this.aAvisar.size() > 0){
        
          for(int i = 0 ; i<this.aAvisar.size() ; i++)
            if((this.aAvisar.get(i).vazio())) this.aAvisar.remove(i);
        }
        
        
        
        
         for(InfoLeilaoFinalizado iL : this.aAvisar){
             s.append(iL.getAviso(usn));
             if(!s.equals("async;")) return s;
         }
         
         
         locker.writeUnlockAti();
         
       
         
         
         
         locker.readLockUlF();
         int temp = this.ultFechado;
         
        
        
            while(this.ultFechado == temp){
               locker.readUnlockUlF();
                wait();
               locker.readLockUlF();
            }
            
            locker.readUnlockUlF();
            
            s.append(this.aAvisar.get(0).getAviso(usn));
            
            
            if(!(s.equals("async:"))) return s; 
            
       }    
     
        
        
        
        
       return s; // pode ser a string sem nada. Ver isso  
        
     
       
    }
    
    
   public double precoLeilao(Integer id){
        double ret = -1;
        locker.readLockAti();
        if (ativos.containsKey(id))
            ret = ativos.get(id).getPreco();
        locker.readUnlockAti();
        return ret;
    }
    
    
    public boolean allFree(){
        return locker.allFree();
    }
}
