package objetos;

import java.sql.Date;

public class Pago {

    int idPagos; 
    String monto; 
    Date fecha; 
    String concepto; 
    int idAlumno; 
    
    public Pago(int idPagos, String monto, Date fecha, String concepto, int idAlumno) {
        this.idPagos = idPagos;
        this.monto = monto;
        this.fecha = fecha;
        this.concepto = concepto;
        this.idAlumno = idAlumno;
    }
    public int getIdPagos() {
        return idPagos;
    }
    public void setIdPagos(int idPagos) {
        this.idPagos = idPagos;
    }
    public String getMonto() {
        return monto;
    }
    public void setMonto(String monto) {
        this.monto = monto;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getConcepto() {
        return concepto;
    }
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
    public int getIdAlumno() {
        return idAlumno;
    }
    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    

}
