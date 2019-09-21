package com.cuna.splashscreen;


import android.content.Intent;

import java.io.Serializable;

public class Category extends Intent implements Serializable {

    private int idTienda;
    private String nombre;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;


    public Category() {

    }

    public Category(int idTienda, String nombre, String descripcion, String direccion, Double latitud, Double longitud) {
        this.idTienda = idTienda;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Category(String nombre, String descripcion, String direccion, Double latitud, Double longitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() {
        return idTienda;
    }

    public void setId(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
