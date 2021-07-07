package controladores;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class RegistrarControllerEmergente implements Initializable {
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

    @FXML
    void guardar(MouseEvent event) throws IOException {
        
        
    }

    public void setDatos(String nombre, int nivel, String codigo){

        this.txtNombre.setText(nombre);
        String lvl = nivelE(nivel);

        
        this.cbNivel.setValue(lvl);
        this.txtCodigo.setText(codigo);
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbNivel.getItems().addAll(niveles);
    }

    private String nivelE(int x){

        String lvl = "";

        switch(x){

            case 1:
                lvl = "A1";
            case 2:
                lvl = "A2";
            break;
            case 3:
                lvl = "B1";
            break;
            case 4:
                lvl = "B2";
            break;
            case 5:
                lvl = "C1";
            break;
            case 6:
                lvl = "C2";
            break;

        }
        return lvl;
    }

}
