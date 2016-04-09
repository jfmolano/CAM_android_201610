package com.example.carolinapinzon.micampusuniandes;

/**
 * Created by carolinapinzon on 4/7/16.
 */
public class SugerenciaDia {

    private String edificio;
    private int ruido;
    private int hora;
    private int iconId;

    public SugerenciaDia(String edificio, int ruido, int hora, int iconId) {
        this.edificio = edificio;
        this.ruido = ruido;
        this.hora = hora;
        this.iconId = iconId;
    }

    public String getEdificio() {
        return edificio;
    }

    public int getRuido() {
        return ruido;
    }

    public int getHora() {
        return hora;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public void setRuido(int ruido) {
        this.ruido = ruido;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
