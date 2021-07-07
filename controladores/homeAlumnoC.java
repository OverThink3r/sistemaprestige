package controladores;



import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class homeAlumnoC {

    
    @FXML
    private AnchorPane anchorALumno;
    
    @FXML
    private Button btnEliminarAlumno;

    @FXML
    private Button btnModificarAlumno;

    @FXML
    private Button btnIngresarAlumno;

    @FXML
    private Button btnConsultarAlumno;

    private BorderPane pane;

    @FXML
    void ingresarAlumno(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ingresarAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) anchorALumno.getParent();
        pane.setCenter(root);
    }

    @FXML
    void modificarAlumno(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/modificarAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) anchorALumno.getParent();
        pane.setCenter(root);
    }

    @FXML
    void consultarAlumno(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/consultarAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) anchorALumno.getParent();
        pane.setCenter(root);
    }

    @FXML
    void eliminarAlumno(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/eliminarAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) anchorALumno.getParent();
        pane.setCenter(root);
    }
    
}
