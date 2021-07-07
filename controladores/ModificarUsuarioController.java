package controladores;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ModificarUsuarioController {
    @FXML
    private TableColumn<?, ?> nombre;

    @FXML
    private TableColumn<?, ?> rol;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btn6;

    @FXML
    private Button btn7;

    @FXML
    private TableColumn<?, ?> apMaterno;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private TableColumn<?, ?> NombreUser;

    @FXML
    private Button btnAplicar;

    @FXML
    private Button btn1;

    @FXML
    private TableColumn<?, ?> apPaterno;

    @FXML
    BorderPane pane;

    @FXML
    void onbtnSalirClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaUsuario.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }
    

    @FXML
    void onbtnAplicarClick(ActionEvent event) {

    }


    @FXML
    void onbtn1Click(ActionEvent event) {

    }

    // @FXML
    // void onbtn1Click(ActionEvent event) {

    // }

    // @FXML
    // void onbtn1Click(ActionEvent event) {

    // }

    // @FXML
    // void onbtn1Click(ActionEvent event) {

    // }

}
