/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
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
    private Locker locker;



	

        // Construtores
    public Leiloeira(){
        this.incrementador = 0;
	this.ativos = new TreeMap<> ();
	this.historico = new TreeMap<> ();
	this.utilizadores = new TreeMap<> ();
        this.locker = new Locker();
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
    }

	// Gets

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

    
    public int addLeilao(String item , Vendedor v , Double p){
        int aux;
        locker.writeLockInc();
        incrementador++;
        aux = incrementador;
        locker.writeUnlockInc();
        
        locker.writeLockAti();
        ativos.put(aux,(new Leilao(item,v,p)));
        locker.writeUnlockAti();
        
	return incrementador;	
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
            tmp.licitar((Comprador) u, valor);
            ret = true;
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
	
    public boolean fecharLeilao(Integer idLeil, String usn){
        Leilao l;
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
        historico.put(idLeil,l);	
	locker.writeUnlockHis();
        
	return true;
    }
}
