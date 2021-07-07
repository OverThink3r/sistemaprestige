package objetos;

import java.sql.Date;

public class Alumno {

    
    int idAlumno; 
    String nombre; 
    String Paterno; 
    String Materno; 
    String telefono; 
    String celular; 
    String correo; 
    Date fechaNac; 
    Date fechaPago; 
    int colegiatura; 
    int nivel; 
    int idGrupo;
    String grupo;
    String nombreResponsable;
    String aPaternoResponsable;
    String aMaternoResponsable;
    String rfc;
    String factura;


     public Alumno(int idAlumno, String nombre, String paterno, String materno, String telefono, String celular,
            String correo, Date fechaNac, Date fechaPago, int colegiatura, int nivel) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.Paterno = paterno;
        this.Materno = materno;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.fechaNac = fechaNac;
        this.fechaPago = fechaPago;
        this.colegiatura = colegiatura;
        this.nivel = nivel;
    }


    public Alumno(int idAlumno, String nombre, String paterno, String materno, String telefono, String celular,
            String correo, Date fechaNac, Date fechaPago, int colegiatura, int nivel, int idGrupo, String grupo,
            String nombreResponsable, String aPaternoResponsable, String aMaternoResponsable, String rfc,
            String factura) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        Paterno = paterno;
        Materno = materno;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.fechaNac = fechaNac;
        this.fechaPago = fechaPago;
        this.colegiatura = colegiatura;
        this.nivel = nivel;
        this.idGrupo = idGrupo;
        this.grupo = grupo;
        this.nombreResponsable = nombreResponsable;
        this.aPaternoResponsable = aPaternoResponsable;
        this.aMaternoResponsable = aMaternoResponsable;
        this.rfc = rfc;
        this.factura = factura;
    }


    public Alumno(int idAlumno, String nombre, String paterno, String materno, String telefono, String celular,
            String correo, Date fechaNac, Date fechaPago, int colegiatura, int nivel, int idGrupo, String grupo) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.Paterno = paterno;
        this.Materno = materno;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.fechaNac = fechaNac;
        this.fechaPago = fechaPago;
        this.colegiatura = colegiatura;
        this.nivel = nivel;
        this.grupo = grupo;
        this.idGrupo = idGrupo;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return Paterno;
    }

    public void setPaterno(String paterno) {
        Paterno = paterno;
    }

    public String getMaterno() {
        return Materno;
    }

    public void setMaterno(String materno) {
        Materno = materno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(int colegiatura) {
        this.colegiatura = colegiatura;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }


    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }


    public String getAPaternoResponsable() {
        return aPaternoResponsable;
    }


    public void setAPaternoResponsable(String aPaternoResponsable) {
        this.aPaternoResponsable = aPaternoResponsable;
    }


    public String getAMaternoResponsable() {
        return aMaternoResponsable;
    }


    public void setAMaternoResponsable(String aMaternoResponsable) {
        this.aMaternoResponsable = aMaternoResponsable;
    }


    public String getRfc() {
        return rfc;
    }


    public void setRfc(String rfc) {
        this.rfc = rfc;
    }


    public String getFactura() {
        return factura;
    }


    public void setFactura(String factura) {
        this.factura = factura;
    }


    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    

    

}
