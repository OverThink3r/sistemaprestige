package objetos;

import java.sql.Date;

public class Bitacora2 {
    Date fecha;
    String tipo;
    String cambio;
    String usuario;

    public Bitacora2(Date fecha, String tipo, String cambio, String usuario) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.cambio = cambio;
        this.usuario = usuario;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getCambio() {
        return cambio;
    }
    public void setCambio(String cambio) {
        this.cambio = cambio;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
}
