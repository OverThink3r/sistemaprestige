package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;

public class controlador_registrar_pago implements Initializable {

    @FXML
    private Button btn_cancelar;

    @FXML
    private DatePicker dp_fecha;

    @FXML
    private Button btn_guardar;

    @FXML
    private TextField Txt_cantidad;

    @FXML
    private TextField Txt_alumno;

    @FXML
    private Label lb_idpago;

    @FXML
    private TextField Txt_concepto;

    @FXML
    private Label lb_cantidad;

    @FXML
    private Label lb_alumno;

    @FXML
    private Label lb_concepto;

    @FXML
    private Label lb_fecha;

    @FXML
    private AnchorPane myAnchor;

    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet res = null;

    @FXML
    void guardar(ActionEvent event) throws SQLException {

        String insertQuery = "INSERT INTO pagos_realizados ( monto, fecha, concepto, idalumno) VALUES (?,?,?,?)";

        String idAlumno = Txt_alumno.getText().trim();
        String concepto = Txt_concepto.getText().trim();
        String cantidad = Txt_cantidad.getText().trim();

       LocalDate dia_actual = LocalDate.now();

       Boolean bandera_fecha=true;

       if(dia_actual.isBefore(dp_fecha.getValue())){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("La fecha ingresada es incorrecta");
        alert.showAndWait();
        bandera_fecha =false;

       }

        if (!idAlumno.isEmpty() && !idAlumno.trim().isEmpty() && idAlumno != null && !concepto.isEmpty()
                && !concepto.trim().isEmpty() && concepto != null && !cantidad.isEmpty() && !cantidad.trim().isEmpty()
                && cantidad != null  && dp_fecha.getValue() != null && bandera_fecha == true) {

            Boolean bandera_catidad = validar_cantidad();
            Boolean bandera_idAlumno = validar_idAlumno();
    
            if (bandera_catidad == true && bandera_idAlumno == true && dp_fecha.getValue() != null) {
                prep = connection.prepareStatement(insertQuery);

                prep.setInt(1, Integer.parseInt(Txt_cantidad.getText()));
                prep.setDate(2, java.sql.Date.valueOf(dp_fecha.getValue().toString()));

                prep.setString(3, Txt_concepto.getText());
                prep.setInt(4, Integer.parseInt(Txt_alumno.getText()));

                int salio = prep.executeUpdate();
                
                if (salio > 0){
                    mensajeExito("Pago Registrado Correctamente");

                    String procedure = "CALL realizarPago(?,?)";
                    prep = connection.prepareStatement(procedure);
                    prep.setInt(1, Integer.parseInt(Txt_cantidad.getText()));
                    prep.setInt(2, Integer.parseInt(Txt_alumno.getText()));
                    prep.executeUpdate();


                    reiniciar_campos();
                    
                
                }
                else{
                    mensajeError("Pago no registrado");}
            } else if (bandera_idAlumno == false) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("El campo IdAlumno no esta llenado correctamente");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("El campo Cantidad no esta llenado correctamente");
                alert.showAndWait();
            }
        } else {
            mostrar_error();
        }

    }

    private BorderPane pane;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_menu_pagos.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }

    public boolean validar_cantidad() {
        String pago = Txt_cantidad.getText();
        Pattern pat = Pattern.compile("\\d+(,\\d+)?");
        Matcher mat = pat.matcher(pago);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validar_idAlumno() {
        String idAlum = Txt_alumno.getText();
        Pattern pat = Pattern.compile("\\d+(,\\d+)?");
        Matcher mat = pat.matcher(idAlum);
        if (mat.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void mostrar_error() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("Porfavor rellena todos los campos");
        alert.showAndWait();
    }


    public void reiniciar_campos(){
        Txt_alumno.setText("");
        Txt_cantidad.setText("");
        Txt_concepto.setText("");
        dp_fecha.setValue(null);

    } 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dp_fecha.getEditor().setDisable(true);
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
    }

    private void mensajeError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    private void mensajeExito(String exito) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Exito");
        alert.setContentText(exito);
        alert.showAndWait();
    }
}