package objetos;

public class Bitacora {
    
    int idMovimiento; 
    String movi; 
    int idAlumno; 
    int idPagos; 
    int idUsuario; 

    public Bitacora(int idMovimiento, String movi, int idAlumno, int idPagos, int idUsuario) {
        this.idMovimiento = idMovimiento;
        this.movi = movi;
        this.idAlumno = idAlumno;
        this.idPagos = idPagos;
        this.idUsuario = idUsuario;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getMovi() {
        return movi;
    }

    public void setMovi(String movi) {
        this.movi = movi;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdPagos() {
        return idPagos;
    }

    public void setIdPagos(int idPagos) {
        this.idPagos = idPagos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    
    
}
