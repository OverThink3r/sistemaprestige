
package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

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


public class controlador_ventana_modificar_pago implements Initializable {

    @FXML
    private TextField Txt_idpago;

    @FXML
    private TextField Txt_alumno;

    @FXML
    private TextField Txt_cantidad;

    @FXML
    private TextField Txt_concepto;

    @FXML
    private Button btn_guardar;

    @FXML
    private Button btn_cancelar;

    @FXML
    private DatePicker dp_fecha;

    @FXML
    private Label lb_idpago;

    @FXML
    private Label lb_alumno;

    @FXML
    private Label lb_cantidad;

    @FXML
    private Label lb_fecha;

    @FXML
    private Label lb_concepto;

    @FXML
    AnchorPane myAnchor;

    Connection connection = null;
    PreparedStatement prep = null;

    @FXML
    void guardar(ActionEvent event) throws SQLException, IOException {

        String query = "UPDATE pagos_realizados SET monto = ?, concepto = ?, idalumno = ?, fecha = ? WHERE idpagos = ?";

         String idAlumno = Txt_alumno.getText().trim();
        String concepto = Txt_concepto.getText().trim();
        String cantidad = Txt_cantidad.getText().trim();

        if (!idAlumno.isEmpty() && !idAlumno.trim().isEmpty() && idAlumno != null && !concepto.isEmpty()
                && !concepto.trim().isEmpty() && concepto != null && !cantidad.isEmpty() && !cantidad.trim().isEmpty()
                && cantidad != null && dp_fecha.getValue() != null) {

        prep = connection.prepareStatement(query);
        


        prep.setInt(1, Integer.parseInt(Txt_cantidad.getText()) );
        prep.setString(2, Txt_concepto.getText());
        prep.setInt(3, Integer.parseInt(Txt_alumno.getText()));
        prep.setDate(4, java.sql.Date.valueOf(dp_fecha.getValue().toString()));
        prep.setInt(5, Integer.parseInt(Txt_idpago.getText()));

        int salio = prep.executeUpdate();

        if(salio > 0) {
            mensajeExito("Modificado Correctamente");
            reiniciar_campos();
            }
        else{
            mensajeError("No se pudo modificar");
            } 
                
            }

        else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("Porfavor rellena todos los campos");
        alert.showAndWait();
        }
    }

    public void reiniciar_campos() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_modificar_pago.fxml"));
        Parent root = loader.load();
        
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);

    } 

    BorderPane pane;
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_modificar_pago.fxml"));
        Parent root = loader.load();
        
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }


      @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        dp_fecha.getEditor().setDisable(true);
        try{
            connection = Conector.conectar();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void setDatos(int id_pago, int monto, Date fecha, String concepto, int id_alumno){
        
        this.Txt_idpago.setText(String.valueOf(id_pago));
        this.Txt_idpago.setDisable(true);
        this.Txt_cantidad.setText(String.valueOf(monto));
         this.dp_fecha.setValue(fecha.toLocalDate());
        this.Txt_concepto.setText(concepto);
        this.Txt_alumno.setText(String.valueOf(id_alumno));
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
