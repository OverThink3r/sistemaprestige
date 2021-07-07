package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Bitacora;
import objetos.Bitacora2;

public class vistaBitacoraController implements Initializable {
    
    @FXML
    private TableColumn<?, ?> idAlumno;

    @FXML
    private Button btnSalir;

    @FXML
    private TableColumn<?, ?> movimiento;

    @FXML
    private TableColumn<?, ?> idMovimiento;

    @FXML
    private TableView<Bitacora2> tablaUsuarios;


    @FXML
    private TableColumn<?, ?> idPagos;
    
    private BorderPane pane;

    @FXML
    private AnchorPane myAnchor;
    @FXML
    void onbtnSalirClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaUsuario.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();

        pane.setCenter(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String query = null;
    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet res = null;
    Bitacora usuarioo = null;

    ObservableList <Bitacora> ListaBitacora = FXCollections.observableArrayList();
    ObservableList <Bitacora2> ListaBitacora2 = FXCollections.observableArrayList();

    private void cargarDatos() throws SQLException {
        connection = Conector.conectar();
        refrescar(); 

        idMovimiento.setCellValueFactory(new PropertyValueFactory<>("fecha")); 
        movimiento.setCellValueFactory(new PropertyValueFactory<>("tipo")); 
        idAlumno.setCellValueFactory(new PropertyValueFactory<>("cambio")); 
        idPagos.setCellValueFactory(new PropertyValueFactory<>("usuario")); 
        
    }

    private void refrescar() throws SQLException {
        ListaBitacora2.clear();
        query = "SELECT * FROM control";
        prep = connection.prepareStatement(query);
        res = prep.executeQuery();

        while(res.next()){

            ListaBitacora2.add( new Bitacora2(res.getDate("fecha"), res.getString("tipo"), res.getString("cambio"), res.getString("usuario")));
        }

        tablaUsuarios.setItems(ListaBitacora2);
    }
}
