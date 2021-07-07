package objetos;


public class Grupos {

    int idgrupo; 
    String codigoGrupo;
    String nombreGrupo; 
    int idnivel; 

    
    public Grupos(int idgrupo, String codigogrupo, String nombregrupo, int idnivel) {
        this.idgrupo = idgrupo;
        this.codigoGrupo = codigogrupo;
        this.nombreGrupo = nombregrupo;
        this.idnivel = idnivel;
    }
    
    public int getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigogrupo) {
        this.codigoGrupo = codigogrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombregrupo) {
        this.nombreGrupo = nombregrupo;
    }

    public int getIdnivel() {
        return idnivel;
    }
    
    public void setIdnivel(int idnivel) {
        this.idnivel = idnivel;
    }
    

}
