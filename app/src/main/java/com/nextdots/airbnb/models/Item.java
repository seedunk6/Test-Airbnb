package com.nextdots.airbnb.models;

/**
 * Created by Mariexi on 03/12/2016.
 */
public class Item {
    private String titulo;
    private int icono;

    public Item(String title, int icon) {
        this.titulo = title;
        this.icono = icon;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
