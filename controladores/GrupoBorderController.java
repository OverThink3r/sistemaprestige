package controladores;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class GrupoBorderController {
    

    @FXML
    private BorderPane borderPaneGrupo;

    @FXML
    void registrar(MouseEvent event) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("/vistas/Registrar.fxml"));
        borderPaneGrupo.setCenter(root);
        
    }
    @FXML
    void modificar(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/ModificarGrupo.fxml"));
        borderPaneGrupo.setCenter(root);
    }

    @FXML
    void consultar(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/ConsultarGrupo.fxml"));
        borderPaneGrupo.setCenter(root);
    }

    @FXML
    void eliminar(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/EliminarGrupo.fxml"));
        borderPaneGrupo.setCenter(root);
    }

    public void setButton(Button boton){

    }

    
}
