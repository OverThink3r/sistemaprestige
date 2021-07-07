package objetos;

public class Usuario {


    int idUsuario; 
    String nombreUsuario; 
    String rol; 
    String nombre; 
    String Paterno; 
    String Materno; 

    public Usuario(int idUsuario, String nombreUsuario, String rol, String nombre, String paterno, String materno) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.nombre = nombre;
        this.Paterno = paterno;
        this.Materno = materno;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }


    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
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

    


    

}
