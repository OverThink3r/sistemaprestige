package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Grupos;
public class RegistrarController implements Initializable{

    @FXML
    private TableColumn<?, ?> icono;

    @FXML
    private CheckBox chbNivel;

    @FXML
    private TextField txtNombre;

    @FXML
    private TableColumn<?, ?> id_grupo;

    @FXML
    private ChoiceBox<String> cbNivel;

    @FXML
    private CheckBox chbCodigo;

    @FXML
    private CheckBox chbNombre;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TableColumn<?, ?> nivel;

    
    private String niveles [] = {"A1", "A2", "B1", "B2", "C1", "C2"};

    Connection connection = null;
    PreparedStatement prep = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbNivel.getItems().addAll(niveles);
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    @FXML
    private AnchorPane myAnchor;
    private BorderPane pane;

    @FXML
    void cancelar(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GrupoBorder.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
        
    }

    int res = 0;
    Grupos grupo = null;
    
    @FXML
    void guardar(MouseEvent event) throws SQLException {

        if (camposVacios()){
            if(verficar()){
                String insertQuery = "INSERT INTO grupo (codigo_grupo, nombre_grupo, idnivel) VALUES (?,?,?)";
                prep = connection.prepareStatement(insertQuery);
                prep.setString(1, txtCodigo.getText());
                prep.setString(2, txtNombre.getText());
                prep.setInt(3, nivel());
                int salio = prep.executeUpdate();
                if(salio > 0) {
                    alertInfo("Se registró correctamente el grupo");
                    txtCodigo.setText("");
                    txtNombre.setText("");
                    cbNivel.setValue(null);

                }
                else alertError("Ocurrío un error");
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
            alertError("Porfavor rellena todos los campos");
        } else return true;

        return false;
    }

    private boolean contieneLetras(String cadena) {
        char c;
        for (int x = 0; x < cadena.length(); x++) {
            c = cadena.charAt(x);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                alertError("Debe contener solo letras");
                return false;
            }
        }
        return true;
    }

    public boolean esAlfanumerico(String str) {

        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c)){
                alertError("No es alfanumerica");
                return false;
            }
                
        }
        return true;
    }

    private int nivel(){

        String nivel = cbNivel.getValue();
        int lvl = 0;

        switch(nivel){

            case "A1":
                lvl = 1;
            case "A2":
                lvl = 2;
            break;
            case "B1":
                lvl = 3;
            break;
            case "B2":
                lvl = 4;
            break;
            case "C1":
                lvl = 5;
            break;
            case "C2":
                lvl = 6;
            break;

        }
        return lvl;
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
