/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leiloes_server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FrVentura
 */
public class InfoLeilaoFinalizado {
    private int id;
    private String vendedor;
    private String vencedor;
    private List<String> licitadores;
    
    
    public InfoLeilaoFinalizado(int idLeilao, Leilao l){
        id = idLeilao;
        vendedor = l.getVendedor().getUsername();
        vencedor = l.getCompradorMaiorLance().getUsername();
        licitadores = new ArrayList<>();
        for (Comprador c : l.getListaLances()){
            licitadores.add(c.getUsername());
        }
    }
    
}
