/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.TreeSet;

/**
 *
 * @author jreis
 */

public class Leilao {

	private String item; // Descrição do que está a ser leiloado
	private Vendedor vendedor; // Vendedor que criou o Leilão
	private Double preco; // Preço inicial ou maior licitação 
	private TreeSet<Comprador> listaLances; // Compradores que licitaram o item
	private Comprador compradorMaiorLance; // Comprador que detem a maior licitação

	// Getters

	public String getItem() {return this.item;}
	public Vendedor getVendedor() {return this.vendedor;}
	public Double getPreco() {return this.preco;}
	public TreeSet<Comprador> getListaLances() {return this.listaLances;}
	public Comprador getCompradorMaiorLance() {return this.compradorMaiorLance;}

	// Métodos

	public Leilao (String item, Vendedor vendedor, Double preco){
		this.item = item;
		this.vendedor = vendedor;
		this.preco = preco;
                listaLances = new TreeSet<>();
                
	}

	public synchronized boolean licitar(Comprador c, Double valor){
		if (valor <= preco) return false;

		this.compradorMaiorLance = c;
		this.preco = valor;
		listaLances.add(c);
		return true;
	}

}