package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.User;
import objetos.UserHolder;

public class UsuarioController implements Initializable {
    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnBitacora;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnConsultar;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnModificar;

    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet res = null;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            connection = Conector.conectar();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        UserHolder holder = UserHolder.getInstance();
        User u = holder.getUser();
        String uname = u.getName();
        try {
            validar(uname);
        } catch (SQLException e) {
            e.printStackTrace();
        }   
    }

    private void validar(String uname) throws SQLException {

        String query = "SELECT rol FROM usuario WHERE nombre_usuario = '"+ uname + "'";

        prep = connection.prepareStatement(query);

        res = prep.executeQuery();

        
        while(res.next()){
            String rol = res.getString("rol");

            if(!rol.equals("ADMINISTRADOR1")){
                btnEliminar.setDisable(true);
                btnModificar.setDisable(true);
            }
        }

    }

    @FXML
    void onbtnRegistrarClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista2.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void onbtnEliminarClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaeliminar.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void onbtnModificarClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaModificarUsuariochido.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void onbtnBitacoraClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaBitacora.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void onbtnConsultarClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaConsultarUsuario.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void onbtnReporteClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaReporte.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    

}
