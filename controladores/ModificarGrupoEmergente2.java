package controladores;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Grupos;

public class ModificarGrupoEmergente2 implements Initializable {
    
    @FXML
    private TextField txtNombre;

    @FXML
    private ChoiceBox<String> cbNivel;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TextField txtCodigo;

    BorderPane pane;

    private String niveles [] = {"A1", "A2", "B1", "B2", "C1", "C2"};

    @FXML
    void cancelar(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ModificarGrupo.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }
    String query = null;
    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet res = null;
    Grupos grupo = null;

    private int idgrupo;
    private int idnivelgrupo;

    @FXML
    void guardar(MouseEvent event) throws SQLException, IOException {

        if (camposVacios()){
            if(verficar()){
                query = "UPDATE grupo SET codigo_grupo = ?, nombre_grupo = ?, idnivel = ? WHERE idgrupo = ?";
                prep = connection.prepareStatement(query);
                prep.setString(1, txtCodigo.getText());
                prep.setString(2, txtNombre.getText());
                prep.setInt(3, idnivelgrupo);
                prep.setInt(4, idgrupo);
                int salio = prep.executeUpdate();
                if(salio>0){
                    alertInfo("Grupo Modificado Correctamente");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ModificarGrupo.fxml"));
                    Parent root = loader.load();
                    pane = (BorderPane) myAnchor.getParent();
                    pane.setCenter(root);
                } 
                else alertError("No se pudo modificar");
            }
        }
    }

    private boolean verficar() {
        String nombre = txtNombre.getText();
        String codigo = txtCodigo.getText();
        if( contieneLetras(nombre) && (esAlfanumerico(codigo) || contieneLetras(codigo)) ){
            return true;
        }
        return false;
    }

    private boolean camposVacios() {
        String nombre = txtNombre.getText();
        String codigo = txtCodigo.getText();
        if(cbNivel.getValue() == null || nombre.trim().isEmpty() || codigo.trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Porfavor rellena todos los campos");
            alert.showAndWait();
        } else return true;

        return false;
    }

    public static boolean contieneLetras(String cadena) {
        char c;
        for (int x = 0; x < cadena.length(); x++) {
            c = cadena.charAt(x);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                return false;
            }
        }
        return true;
    }

    public boolean esAlfanumerico(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c == '-' || c == ' ' || c == '#' || c == '$') continue;
            if (!Character.isLetterOrDigit(c))
                return false;
        }
        return true;
    }
    

    public void setDatos(String name, int nvl, String code, int idgrupoo){
        txtNombre.setText(name);
        idnivelgrupo = nvl;
        cbNivel.setValue(lvl(nvl));
        txtCodigo.setText(code);
        this.idgrupo = idgrupoo;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbNivel.getItems().addAll(niveles);
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String lvl (int x) {
        String codigo = "";
        switch(x){
            case 1:
            codigo = "A1";
            break;

            case 2:
            codigo = "A2";
            break;

            case 3:
            codigo = "B1";
            break;

            case 4:
            codigo = "B2";
            break;

            case 5:
            codigo = "C1";
            break;

            case 6:
            codigo = "C2";
            break;
        }
        return codigo;
    }
    private void alertError(String cuerpo){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(cuerpo);
        alert.showAndWait();
    }

    private void alertInfo(String cuerpo){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(cuerpo);
        alert.setContentText(cuerpo);
        alert.showAndWait();
    }
}
