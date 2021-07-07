package controladores;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;

public class vistaModificar2Controller implements Initializable{
    @FXML
    private TextField txtNombreUser;

    @FXML
    private ChoiceBox<String> cbRol;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblApPaterno;

    @FXML
    private Button btnGuardar;

    @FXML
    private Label lblNombreUser;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtApMaterno;

    @FXML
    private TextField txtApPaterno;

    @FXML
    private Label lblRol;

    @FXML
    private Label lblApMaterno;
    @FXML
    private String roles [] = {"ADMINISTRADOR","TRABAJADOR"};


    @FXML
    AnchorPane myAnchor;
    
    private BorderPane pane;

    @FXML
    void onbtnCancelarClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaModificarUsuariochido.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();

        pane.setCenter(root);
    }
    private int idUsuario;
    public void setDatos(String nombre, String apPaterno, String apMaterno, String user, String rol, int iduser){
        txtNombre.setText(nombre);
        txtApPaterno.setText(apPaterno);
        txtApMaterno.setText(apMaterno);
        txtNombreUser.setText(user);
        cbRol.setValue(rol);
        idUsuario = iduser;
    }
    String query = null;
    Connection connection;
    PreparedStatement prep = null;
    @FXML
    void onbtnGuardarClick(ActionEvent event) throws SQLException, IOException {
        if(validarVacio() && validarCadenas()){
        query = "UPDATE usuario SET nombre_usuario = ? , rol = ?, nombre = ?, apaterno = ?, amaterno = ? WHERE idusuario = ?";
        prep = connection.prepareStatement(query);
        prep.setString(1, txtNombreUser.getText());
        prep.setString(2, cbRol.getValue());
        prep.setString(3, txtNombre.getText());
        prep.setString(4, txtApPaterno.getText());
        prep.setString(5, txtApMaterno.getText());
        prep.setInt(6, idUsuario);

        int salio = prep.executeUpdate();
        
        if(salio>0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Exitoso");
        alert.setContentText("Modificado Correctamente");
        alert.showAndWait();

        } else alerta("Ocurrió un error al intentar modificar");
        onbtnCancelarClick(event);
        }
 }
  private boolean validarVacio() {
        String nombre = txtNombre.getText().trim();
        String paterno = txtApPaterno.getText().trim();
        String usuario = txtNombreUser.getText().trim();

        if (nombre.isEmpty() || paterno.isEmpty() || usuario.isEmpty() || cbRol == null ) {
            alerta("Campos vacíos");
            return false;
        } else
            return true;

    }
       private boolean soloLetras(String cadena) {

    for (int i = 0; i < cadena.length(); i++) {
        char c = cadena.charAt(i);
        if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
            return false;
        }
    }
    return true;
}
private boolean validarCadenas(){
    if(!soloLetras(txtNombre.getText()) || !soloLetras(txtApPaterno.getText()) || !soloLetras(txtApMaterno.getText())){
        alerta("El campo solo puede contener letras");
        return false;
    }
    else return true;
}
     private void alerta(String alerta) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(alerta);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cbRol.getItems().addAll(roles);
         try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
