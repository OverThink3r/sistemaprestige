package controladores;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import modelos.Conector;
import objetos.User;
import objetos.UserHolder;
import objetos.encrypt;

public class logincontroler implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField passw;

    @FXML
    private Button entrar;

    @FXML
    private ChoiceBox<String> userlist;

    @FXML
    private Button recpass;

    private ArrayList<String> users = new ArrayList<String>();

    private double x, y;

    Connection connection;
    PreparedStatement prep;
    ResultSet res;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            connection = Conector.conectar();
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
        } catch (SQLException e) {

        }

    }


    @FXML
    void loginEnter(KeyEvent event) throws IOException {

        if (event.getCode() == KeyCode.ENTER) {

            User u = new User();
            String password = passw.getText().trim();
            if (userlist.getValue() != null && password != null && !password.isEmpty() && !password.trim().isEmpty()) {

                try {
                    connection = Conector.conectar();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String query = "SELECT contraseña FROM usuario WHERE nombre_usuario = '" + userlist.getValue() + "'";
                try {
                    prep = connection.prepareStatement(query);
                    res = prep.executeQuery();
                    res.next();
                    byte[] combined = res.getBytes("contraseña");
                    encrypt enc = new encrypt();
                    if (enc.verifypass(combined, password)) {
                        u.setName(userlist.getValue());
                        UserHolder holder = UserHolder.getInstance();
                        holder.setUser(u);
                        root = FXMLLoader.load(getClass().getResource("/vistas/Main.fxml"));

                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);

                        scene.setFill(Color.TRANSPARENT);

                        root.setOnMousePressed(new EventHandler<MouseEvent>() {

                            @Override
                            public void handle(MouseEvent arg0) {
                                x = arg0.getSceneX();
                                y = arg0.getSceneY();

                            }

                        });

                        root.setOnMouseDragged(new EventHandler<MouseEvent>() {

                            @Override
                            public void handle(MouseEvent arg0) {
                                stage.setX(arg0.getSceneX() - x);
                                stage.setY(arg0.getSceneY() - y);
                            }
                        });

                        stage.setScene(scene);

                        stage.show();
                        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
                    } else {
                        Alert al = new Alert(Alert.AlertType.ERROR);
                        al.setTitle("Error");
                        al.setContentText("Contraseña incorrecta");
                        al.setHeaderText(null);
                        al.show();
                    }

                } catch (SQLException e) {
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

    @FXML
    public void login(ActionEvent event) throws IOException {
        User u = new User();
        String password = passw.getText().trim();
        if (userlist.getValue() != null && password != null && !password.isEmpty() && !password.trim().isEmpty()) {

            try {
                connection = Conector.conectar();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            String query = "SELECT contraseña FROM usuario WHERE nombre_usuario = '" + userlist.getValue() + "'";
            try {
                prep = connection.prepareStatement(query);
                res = prep.executeQuery();
                res.next();
                byte[] combined = res.getBytes("contraseña");
                encrypt enc = new encrypt();
                if (enc.verifypass(combined, password)) {
                    u.setName(userlist.getValue());
                    UserHolder holder = UserHolder.getInstance();
                    holder.setUser(u);
                    root = FXMLLoader.load(getClass().getResource("/vistas/Main.fxml"));

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);

                    scene.setFill(Color.TRANSPARENT);

                    root.setOnMousePressed(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent arg0) {
                            x = arg0.getSceneX();
                            y = arg0.getSceneY();

                        }

                    });

                    root.setOnMouseDragged(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent arg0) {
                            stage.setX(arg0.getSceneX() - x);
                            stage.setY(arg0.getSceneY() - y);
                        }
                    });

                    stage.setScene(scene);

                    stage.show();
                    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
                } else {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Error");
                    al.setContentText("Contraseña incorrecta");
                    al.setHeaderText(null);
                    al.show();
                }

            } catch (SQLException e) {
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

    @FXML
    public void loginrecpass1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/vistas/vistarecpass1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
    }

}
