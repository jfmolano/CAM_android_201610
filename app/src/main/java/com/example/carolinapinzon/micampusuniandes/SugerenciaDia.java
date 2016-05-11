package com.example.carolinapinzon.micampusuniandes;

/**
 * Created by carolinapinzon on 4/7/16.
 */
public class SugerenciaDia {

    private String edificio;
    private int ruido;
    private int luz;
    private int temperatura;
    private int humedad;
    private int hora;
    private int iconId;
    private SugerenciaDia s1;
    private SugerenciaDia s2;

    public SugerenciaDia(String edificio, int ruido, int luz, int temperatura, int humedad, int hora, int iconId,SugerenciaDia s1,SugerenciaDia s2) {
        this.edificio = edificio;
        this.ruido = ruido;
        this.luz = luz;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.hora = hora;
        this.iconId = iconId;
        this.s1 = s1;
        this.s2 = s2;
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

    public int getLuz() {
        return luz;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public int getHumedad() {
        return humedad;
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

    public SugerenciaDia getSug1() {
        return s1;
    }

    public SugerenciaDia getSug2() {
        return s2;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
