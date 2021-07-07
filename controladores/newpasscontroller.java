package controladores;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modelos.Conector;
import javafx.scene.Node;

import objetos.encrypt;

public class newpasscontroller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection connection;
    PreparedStatement prep;
    ResultSet res;

    @FXML
    private PasswordField npass;

    @FXML
    private PasswordField rnpass;

    private String user;

    public void setuser(String name) {
        user = name;
    }

    @FXML
    public void newpassrecpass2(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void enviar(ActionEvent event) throws IOException {
        String newpassword = npass.getText().trim();
        String rnewpassword = rnpass.getText().trim();
        if (newpassword != null && !newpassword.isEmpty() && !newpassword.trim().isEmpty() && rnewpassword != null
                && !rnewpassword.isEmpty() && !rnewpassword.trim().isEmpty()) {
            if (newpassword.equals(rnewpassword)) {

                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Confirmacion");
                al.setContentText("Esta seguro?");
                al.setHeaderText("Su contraseña cambiara");

                Optional<ButtonType> result = al.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        connection = Conector.conectar();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    encrypt enc = new encrypt();
                    byte[] hsh = enc.encpass(newpassword);
                    String query = "UPDATE usuario SET contraseña = ? WHERE nombre_usuario = '" + user + "'";
                    try {
                        prep = connection.prepareStatement(query);
                        prep.setBytes(1, hsh);
                        int ef = prep.executeUpdate();
                        if (ef == 1) {
                            Alert alrt = new Alert(Alert.AlertType.INFORMATION);
                            alrt.setTitle("Informacion");
                            alrt.setContentText("Sera redirigido a la ventana principal");
                            alrt.setHeaderText("Su contraseña ha sido modificada exitosamente");
                            alrt.show();
                        } else {
                            Alert ale = new Alert(Alert.AlertType.ERROR);
                            al.setTitle("Error");
                            ale.setContentText("No se pudo cambiar su contraseña");
                            ale.setHeaderText(null);
                            ale.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
                }

            } else {
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error");
                al.setContentText("Las contraseñas no coinciden");
                al.setHeaderText(null);
                al.show();
            }
        } else {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error");
            al.setContentText("Alguno de los campos esta vacio");
            al.setHeaderText(null);
            al.show();
        }
    }

}
