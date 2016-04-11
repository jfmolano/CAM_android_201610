package com.example.carolinapinzon.micampusuniandes;

/**
 * Created by JFM on 09/04/2016.
 */
public class Registro {

    private int hora;
    private String confianza;
    private int lugar;
    private int ruido;

    public Registro(int horaP, String confianzaP, int lugarP, int ruidoP)
    {
        hora = horaP;
        confianza = confianzaP;
        lugar = lugarP;
        ruido = ruidoP;
    }

    public int darHora()
    {
        return hora;
    }

    public String darConfianza()
    {
        return confianza;
    }

    public int darLugar()
    {
        return lugar;
    }

    public int darRuido()
    {
        return ruido;
    }

    public String toString()
    {
        return "hora: "+hora+"; confianza: "+confianza+"; lugar: "+lugar+"; ruido: "+ruido;
    }
}
