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
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modelos.Conector;
import javafx.scene.Node;

public class recpass1controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField res1;

    @FXML
    private TextField res2;

    @FXML
    private ChoiceBox<String> quest1;

    @FXML
    private ChoiceBox<String> quest2;

    @FXML
    private ChoiceBox<String> userlist;

    Connection connection;
    PreparedStatement prep;
    ResultSet res;

    private ArrayList<String> users = new ArrayList<String>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT nombre_usuario FROM usuario";

        try {
            prep = connection.prepareStatement(query);
            res = prep.executeQuery();
            while (res.next()) {
                users.add(res.getString("nombre_usuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userlist.getItems().addAll(users);
        userlist.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> setvalues(userlist.getValue()));

    }

    public void setvalues(String name) {
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query1 = "SELECT preguntas.preguntascol FROM preguntas INNER JOIN  usuario ON usuario.idpreguntas_1 = preguntas.idpreguntas WHERE usuario.nombre_usuario = '"
                + name + "'";
        String query2 = "SELECT preguntas.preguntascol FROM preguntas INNER JOIN  usuario ON usuario.idpreguntas_2 = preguntas.idpreguntas WHERE usuario.nombre_usuario = '"
                + name + "'";
        try {
            prep = connection.prepareStatement(query1);
            res = prep.executeQuery();
            res.next();
            quest1.getItems().add(res.getString("preguntascol"));
            quest1.setValue(res.getString("preguntascol"));
            prep = connection.prepareStatement(query2);
            res = prep.executeQuery();
            res.next();
            quest2.getItems().add(res.getString("preguntascol"));
            quest2.setValue(res.getString("preguntascol"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void recpass1login(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
    }

    @FXML
    public void enviarrecpass(ActionEvent event) throws IOException {
        String ans1 = res1.getText().trim();
        String ans2 = res2.getText().trim();
        if (userlist.getValue() != null && quest1.getValue() != null && quest2.getValue() != null && ans1 != null
                && !ans1.isEmpty() && !ans1.trim().isEmpty() && ans2 != null && !ans2.isEmpty()
                && !ans2.trim().isEmpty()) {
            try {
                connection = Conector.conectar();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            String query1 = "SELECT R_1 FROM usuario WHERE nombre_usuario = '" + userlist.getValue() + "'";
            String query2 = "SELECT R_2 FROM usuario WHERE nombre_usuario = '" + userlist.getValue() + "'";
            try {
                String r1, r2;
                prep = connection.prepareStatement(query1);
                res = prep.executeQuery();
                res.next();
                r1 = res.getString("R_1");
                prep = connection.prepareStatement(query2);
                res = prep.executeQuery();
                res.next();
                r2 = res.getString("R_2");
                if (ans1.equals(r1) && ans2.equals(r2)) {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistanewpass.fxml"));
                    root = loader.load();
                    newpasscontroller newpasscontroller = loader.getController();
                    newpasscontroller.setuser(userlist.getValue());

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
                } else {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Error");
                    al.setContentText("Alguna de las contraseñas es incorrecta");
                    al.setHeaderText(null);
                    al.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
