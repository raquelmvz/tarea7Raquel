/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea7Raquel;

import java.time.LocalDate;



/**
 *
 * @author raquel
 */
public class Empleado {
    
    private String nombre;
    private String dni;
    private String puesto;
    private LocalDate fecTomaPosesion;
    private LocalDate fecCese;
    private String telefono;
    private boolean esEvaluador;
    private boolean esCoordinador;
    
    /* Getters y setters */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public LocalDate getFecTomaPosesion() {
        return fecTomaPosesion;
    }

    public void setFecTomaPosesion(LocalDate fecTomaPosesion) {
        this.fecTomaPosesion = fecTomaPosesion;
    }

    public LocalDate getFecCese() {
        return fecCese;
    }

    public void setFecCese(LocalDate fecCese) {
        this.fecCese = fecCese;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEsEvaluador() {
        return esEvaluador;
    }

    public void setEsEvaluador(boolean esEvaluador) {
        this.esEvaluador = esEvaluador;
    }

    public boolean isEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }
    
    /* to string */

    @Override
    public String toString() {
        return "Empleado{" + "nombre=" + nombre + ", dni=" + dni + ", puesto=" + puesto + ", fecTomaPosesion=" + fecTomaPosesion + ", fecCese=" + fecCese + ", telefono=" + telefono + ", esEvaluador=" + esEvaluador + ", esCoordinador=" + esCoordinador + '}';
    }
    
    
}
