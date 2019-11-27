
package modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Contactos implements Serializable{
    private String nif;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private long telefono;
    private Date nacimiento;
    private String tipo;

    public Contactos(String nif, String nombre, String apellido1, String apellido2, long telefono, Date nacimiento, String tipo) {
        this.nif = nif;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.telefono = telefono;
        this.nacimiento = nacimiento;
        this.tipo = tipo;
    }
    
    public String formatDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.nacimiento);
    }

    public Contactos() {
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Contactos{" + "nif=" + nif + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2=" + apellido2 + ", telefono=" + telefono + ", nacimiento=" + nacimiento + ", tipo=" + tipo + '}';
    }
    
    
}
