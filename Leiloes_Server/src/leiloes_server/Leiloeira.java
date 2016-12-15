/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Renato
 */
public class Leiloeira {

    private int incrementador;
    private TreeMap<Integer,Leilao> ativos; // 
    private TreeMap<Integer,Leilao> historico; // tem todos os Leilões que já acabaram
    private TreeMap<String,Utilizador> utilizadores; // A string é o usn do utilizador.

	

        // Construtores
    public Leiloeira(){
        this.incrementador = 0;
	this.ativos = new TreeMap<> ();
	this.historico = new TreeMap<> ();
	this.utilizadores = new TreeMap<> ();
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

    public synchronized int addLeilao(String item , Vendedor v , Double p){
	this.incrementador ++;
	this.ativos.put(this.incrementador,(new Leilao(item,v,p)));
	return this.incrementador;	
    }


    public synchronized boolean addUtilizador(Utilizador u){
        if(utilizadores.containsKey(u.getUsername()))
            return false;
	else{
            this.utilizadores.put(u.getUsername(),u);
            return true;
        }
    }
    
	// Modo 1 -> Vendedor
	// Modo 2 -> Comprador
    public boolean authenticate(String usn , String pw ,int modo){
        boolean ret = false;
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
        return ret;
    } 	

	

    //public Licitar (Integer idLeil, String usn, double valor){}
	
    //public String ListarLeiloes(String usn){}
	
    public boolean fecharLeilao(Integer idLeil, String usn){
        Leilao l;
        synchronized(ativos){
            if(ativos.containsKey(idLeil)){
                l = ativos.get(idLeil);
                if(l.getVendedor().getUsername().equals(usn))
                    ativos.remove(idLeil);
                else
                    return false;
            }
            else
                return false;
	}
	synchronized(historico){
            historico.put(idLeil,l);	
	}
	return true;
    }

}