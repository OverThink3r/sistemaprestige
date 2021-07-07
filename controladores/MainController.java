package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelos.Conector;
import objetos.User;
import objetos.UserHolder;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Scene;

public class MainController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button inicio;

    @FXML
    private Button alumno;

    @FXML
    private Button usuario;

    @FXML
    private Button pago;

    @FXML
    private Button grupo;

    @FXML
    private Button ajustes;

    @FXML
    private Button salir;

    private String viewAnterior = "";
    private String userName = ""; 
    private String role = "";

    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet res = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = Conector.conectar();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        UserHolder holder = UserHolder.getInstance();
        User u = holder.getUser();
        String uname = u.getName();
        userName = uname;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistahome2.fxml"));
        Parent root;
        try {
            root = loader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            habilitar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    private void habilitar() throws SQLException {
        String query = "SELECT rol FROM usuario WHERE nombre_usuario = '"+ userName + "'";
        
        prep = connection.prepareStatement(query);

        res = prep.executeQuery();
        
        while(res.next()){
            String rol = res.getString("rol");
            role = rol;

            if(!rol.equals("ADMINISTRADOR1") && !rol.equals("ADMINISTRADOR2")){
                usuario.setDisable(true);
                grupo.setDisable(true);
            }
        }
    }

    @FXML
    void cambiarHome(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistahome2.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
        setBtnStyle(inicio);

    }

    @FXML
    void cambiarStudents(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/homeAlumno.fxml"));
        Parent root = loader.load();

        borderPane.setCenter(root);
        setBtnStyle(alumno);

    }

    @FXML
    void cambiarUsers(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaUsuario.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
        setBtnStyle(usuario);

    }

    @FXML
    void cambiarPayments(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_menu_pagos.fxml"));
        Parent root = loader.load();

        borderPane.setCenter(root);
        setBtnStyle(pago);

    }

    @FXML
    void cambiarSettings(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaconfighome.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
        setBtnStyle(ajustes);
    }

    @FXML
    void cambiarGroups(MouseEvent event) throws IOException {
        viewAnterior = "GrupoBorder";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GrupoBorder.fxml"));
        Parent root = loader.load();

        borderPane.setCenter(root);
        setBtnStyle(grupo);

    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void salir(MouseEvent event) throws IOException {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Confirmacion");
        al.setContentText("Esta seguro?");
        al.setHeaderText("Esta apunto de salir");

        Optional<ButtonType> result = al.showAndWait();
        if (result.get() == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    void cambiarGroups() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GrupoBorder.fxml"));
        Parent root = loader.load();

        borderPane.setCenter(root);
    }

    @FXML
    void regresar(MouseEvent event) throws IOException {
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("/vistas/" + viewAnterior + ".fxml")));

    }

    public void setViewAnterior(String view) {
        this.viewAnterior = view;
    }


    private void setBtnStyle(Button btn) {
        resetBtnStyle();
        btn.getStyleClass().add("active");
    }

    private void resetBtnStyle() {
        inicio.getStyleClass().remove("active");
        alumno.getStyleClass().remove("active");
        usuario.getStyleClass().remove("active");
        pago.getStyleClass().remove("active");
        grupo.getStyleClass().remove("active");
        ajustes.getStyleClass().remove("active");
        salir.getStyleClass().remove("active");

    }
}
