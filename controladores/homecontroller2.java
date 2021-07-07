package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import objetos.User;
import objetos.UserHolder;

public class homecontroller2 implements Initializable{
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    UserHolder holder = UserHolder.getInstance();
    User u = holder.getUser();
    String uname = u.getName();

    @FXML
    private BorderPane bphome;

    @FXML
    private Label labelUser;
    
        @FXML
    void cambiarContra(ActionEvent event) {
        
    }

    @FXML
    void cambiarUsuario(ActionEvent event) throws IOException {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Confirmacion");
        al.setContentText("Esta seguro?");
        al.setHeaderText("Esta apunto de cambiar de usuario");

        Optional<ButtonType> result = al.showAndWait();
        if (result.get() == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void configuracion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaconfighome.fxml"));
        Parent root = loader.load();
        bphome.setCenter(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelUser.setText(uname);
        
    }
}
