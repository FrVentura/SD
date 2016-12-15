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
 * @author FrVentura
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

    public Leiloeira(Leiloeira leil){
        this.incrementador = leil.getIncrementador();
	this.utilizadores = new TreeMap<>();
        Utilizador u;
        for(String usn : leil.getUtilizadoresShallow().keySet()){
            u = leil.getUtilizadorShallow(usn);
            if (u instanceof Vendedor){
                this.utilizadores.put(usn,((Vendedor) u).clone());
            }
            else{
                this.utilizadores.put(usn,((Comprador) u).clone());
            }
        }
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

    public Utilizador getUtilizadorShallow(String usn){
        return this.utilizadores.get(usn);
    }

	
    public Leilao getLeilaoHistoricoShallow(Integer n){ 
	return this.historico.get(n); 
    }
	

	
    public TreeMap<Integer,Leilao> getAtivosDeep(){
        TreeMap<Integer,Leilao> ret = new TreeMap<>();
        for (Map.Entry<Integer,Leilao> entry : ativos.entrySet()){
            ret.put(entry.getKey(), entry.getValue().clone());
        }
        return ret;
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
        if(this.utilizadores.containsKey(u.getUsername()))
            return false;
	else{
            this.utilizadores.put(u.getUsername(),u);
            return true;
        }
    }
    
	// Modo 1 -> Vendedor
	// Modo 2 -> Comprador
	public boolean authenticate(String usn , String pw ,int modo)    
	{

		if (this.utilizadores.containsKey(usn)) 
		{
			switch(modo){

				case 1: if(this.utilizadores.get(usn).getClass() == Vendedor.class && (this.utilizadores.get(usn).getPwd().equals(pw))) return true;
					break;
				}
				case 2: if(this.utilizador.get(usn).getClass() == Comprador.class && (this.utilizador.get(usn).getPwd().equals(pw))) return true; 
					default: return false;


			}



		
		 return false;
		 } 	

	

	public Licitar (Integer idLeil, String usn, double valor)
	{

		



	}



	
	public String ListarLeiloes(String usn)
	{



	}
	





	public boolean fecharLeilao(Integer idLeil, String usn)
	{

		Leilao l;


			synchronized(this.ativos){

				if(this.ativos.containsKey(idLeil)){
					l = this.ativos.get(idLeil);
							
							if(this.ativos.getVendedor().getUsername().equals(usn))
								this.ativos.remove(idLeil);
							else return false;

				}else return false;
			}
			


			synchronized(this.historico){
				this.historico.put(idLeil,l);	
			}
					

			return true;
				


			 
			


	}










}