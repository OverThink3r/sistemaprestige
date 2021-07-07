package objetos;

import java.sql.Date;

public class Adeudo {

    //adeudos
    int idAdeudos; //idadeudos
    int monto; //monto
    int idAlumno; //idalumno
    Date mes; //mes
    int pagado; //pagado
    
    public Adeudo(int idAdeudos, int monto, int idAlumno, Date mes, int pagado) {
        this.idAdeudos = idAdeudos;
        this.monto = monto;
        this.idAlumno = idAlumno;
        this.mes = mes;
        this.pagado = pagado;
    }
    public int getIdAdeudos() {
        return idAdeudos;
    }
    public void setIdAdeudos(int idAdeudos) {
        this.idAdeudos = idAdeudos;
    }
    public int getMonto() {
        return monto;
    }
    public void setMonto(int monto) {
        this.monto = monto;
    }
    public int getIdAlumno() {
        return idAlumno;
    }
    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }
    public Date getMes() {
        return mes;
    }
    public void setMes(Date mes) {
        this.mes = mes;
    }
    public int getPagado() {
        return pagado;
    }
    public void setPagado(int pagado) {
        this.pagado = pagado;
    }

    
}
