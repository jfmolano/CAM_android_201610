package com.example.carolinapinzon.micampusuniandes;

import java.util.ArrayList;

/**
 * Created by carolinapinzon on 4/8/16.
 */
public class Instancia {

    private ArrayList<SugerenciaDia> sugerencias;


    private static Instancia instancia;

    public static Instancia darInstancia(){
        if(instancia == null)
        {
            instancia = new Instancia();
        }
        return instancia;
    }

    public Instancia(){
        sugerencias= new ArrayList<SugerenciaDia>();
    }

    public ArrayList<SugerenciaDia> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(ArrayList<SugerenciaDia> sugerencias) {
        this.sugerencias = sugerencias;
    }

    public void agregarSugerencia(SugerenciaDia i) {
        sugerencias.add(i);
    }

    public SugerenciaDia darSugerencia(int i) {
        if (i < sugerencias.size()) {
            return sugerencias.get(i);
        }
        else {
            return null;
        }
    }
}
