package com.example.carolinapinzon.micampusuniandes;

/**
 * Created by JFM on 09/04/2016.
 */
public class Registro {

    private int hora;
    private String confianza;
    private int lugar;
    private int ruido;
    private int luz;
    private int temperatura;
    private int humedad;

    public Registro(int horaP, String confianzaP, int lugarP, int ruidoP, int luzP, int temperaturaP, int humedadP)
    {
        hora = horaP;
        confianza = confianzaP;
        lugar = lugarP;
        ruido = ruidoP;
        luz = luzP;
        temperatura = temperaturaP;
        humedad = humedadP;
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

    public int darLuz()
    {
        return luz;
    }

    public int darTemperatura()
    {
        return temperatura;
    }

    public int darHumedad()
    {
        return humedad;
    }

    public String toString()
    {
        return "hora: "+hora+"; confianza: "+confianza+"; lugar: "+lugar+"; ruido: "+ruido+"; luz: "+luz+"; temperatura: "+temperatura+"; humedad: "+humedad;
    }
}
