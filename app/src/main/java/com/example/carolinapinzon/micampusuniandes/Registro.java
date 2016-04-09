package com.example.carolinapinzon.micampusuniandes;

/**
 * Created by JFM on 09/04/2016.
 */
public class Registro {

    private int hora;
    private int dia;
    private int lugar;
    private int ruido;

    public Registro(int horaP, int diaP, int lugarP, int ruidoP)
    {
        hora = horaP;
        dia = diaP;
        lugar = lugarP;
        ruido = ruidoP;
    }

    public int darHora()
    {
        return hora;
    }

    public int darDia()
    {
        return dia;
    }

    public int darLugar()
    {
        return lugar;
    }

    public int darRuido()
    {
        return ruido;
    }
}
