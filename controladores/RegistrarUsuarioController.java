package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.encrypt;
import javafx.scene.control.Alert;

public class RegistrarUsuarioController implements Initializable {

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblApPaterno;

    @FXML
    private Label lblContraseña;

    @FXML
    private PasswordField txtconfirmarContra;

    @FXML
    private Label lblPregunta1;

    @FXML
    private TextField txtNombreUser;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button btnGuardar;

    @FXML
    private Label lblNombreUser;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtApPaterno;

    @FXML
    private TextField txtApMaterno;

    @FXML
    private Label lblRol;

    @FXML
    private Label lblApMaterno;

    @FXML
    private Label lblConfirmacion;

    @FXML
    private ChoiceBox<String> cbPregunta1;

    @FXML
    private ChoiceBox<String> cbPregunta2;

    @FXML
    private ChoiceBox<String> cbRol;

    private String roles[] = { "ADMINISTRADOR1", "TRABAJADOR", "ADMINISTRADOR2" };

    private ArrayList<String> preguntas = new ArrayList<String>();

    private BorderPane pane;

    @FXML
    private AnchorPane myAnchor;

    Connection connection;
    PreparedStatement prep;
    ResultSet res;

    @FXML
    void onbtnCancelarClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaUsuario.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }

    @FXML
    void onbtnGuardarClick(ActionEvent event) throws SQLException {
        if (validarVacio() && validarContraseña() && validarCadenas() && validarPreguntas()) {
            try {
                connection = Conector.conectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String queryInsert = "INSERT INTO usuario (nombre_usuario,contraseña,idpreguntas_1,idpreguntas_2,rol,nombre,apaterno,amaterno) VALUES (?,?,?,?,?,?,?,?) ";
            encrypt enc = new encrypt();
            byte[] hsh = enc.encpass(txtContraseña.getText().trim());
            try {
                int salio = 0;
                prep = connection.prepareStatement(queryInsert);
                prep.setString(1, txtNombreUser.getText().trim());
                prep.setBytes(2, hsh);
                prep.setInt(3, preguntaId(cbPregunta1.getValue()));
                prep.setInt(4, preguntaId(cbPregunta2.getValue()));
                prep.setString(5, cbRol.getValue());
                prep.setString(6, txtNombre.getText().trim());
                prep.setString(7, txtApPaterno.getText().trim());
                prep.setString(8, txtApMaterno.getText().trim());
                salio = prep.executeUpdate();
                if (salio > 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Exitoso");
                    alert.setContentText("Registrado Correctamente");
                    alert.showAndWait();

                } else
                    alerta("Error al registrar al Usuario");
                vaciar();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void vaciar() {
        txtNombre.setText("");
        txtApPaterno.setText("");
        txtApMaterno.setText("");
        txtNombreUser.setText("");
        txtContraseña.setText("");
        txtconfirmarContra.setText("");
        cbRol.setValue(null);
        cbPregunta1.setValue(null);
        cbPregunta2.setValue(null);
    }

    private boolean validarVacio() {
        String nombre = txtNombre.getText().trim();
        String paterno = txtApPaterno.getText().trim();
        // String materno = txtApMaterno.getText().trim();
        String usuario = txtNombreUser.getText().trim();
        String contraseña = txtContraseña.getText();
        String vcontraseña = txtconfirmarContra.getText();

        if (nombre.isEmpty() || paterno.isEmpty() || usuario.isEmpty() || cbRol == null || cbPregunta1 == null
                || cbPregunta2 == null || contraseña.isEmpty() || vcontraseña.isEmpty()) {
            alerta("Campos vacíos");
            return false;
        } else
            return true;

    }

    private boolean validarContraseña() {
        String contraseña = txtContraseña.getText();
        String vcontraseña = txtconfirmarContra.getText();
        if (!contraseña.equals(vcontraseña)) {
            alerta("La confirmación de contraseña no coincide");
            txtconfirmarContra.setText("");
            return false;
        } else
            return true;
    }

    private boolean soloLetras(String cadena) {

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (c == 'ñ' || c == 'Ñ')
                continue;
            if(c == 'á' || c == 'Á' || c == 'é' || c == 'É' || c == 'í' || c == 'Í' || c == 'ó' || c == 'Ó' || c == 'ú' || c == 'Ú')
                continue;
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                return false;
            }
        }
        return true;
    }

    private boolean validarCadenas() {
        if (!soloLetras(txtNombre.getText()) || !soloLetras(txtApPaterno.getText())
                || !soloLetras(txtApMaterno.getText())) {
            alerta("El campo solo puede contener letras");
            return false;
        } else
            return true;
    }

    private boolean validarPreguntas() {
        if (cbPregunta1.getValue().equals(cbPregunta2.getValue())) {
            alerta("Elija dos preguntas diferentes");
            return false;
        } else
            return true;
    }

    private void alerta(String alerta) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(alerta);
        alert.showAndWait();
    }

    private int preguntaId(String preg) {
        Connection connection2 = null;
        PreparedStatement prep2;
        ResultSet res2;
        int x = 0;
        try {
            connection2 = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT idpreguntas FROM preguntas WHERE preguntascol = ?";

        try {
            prep2 = connection2.prepareStatement(query);
            prep2.setString(1, preg);
            res2 = prep2.executeQuery();
            res2.next();
            x = res2.getInt("idpreguntas");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return x;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT preguntascol FROM preguntas;";

        try {
            prep = connection.prepareStatement(query);
            res = prep.executeQuery();
            while (res.next()) {
                preguntas.add(res.getString("preguntascol"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbPregunta1.getItems().addAll(preguntas);
        cbPregunta2.getItems().addAll(preguntas);
        cbRol.getItems().addAll(roles);

        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
