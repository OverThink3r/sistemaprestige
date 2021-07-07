package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Alumno;

public class alumnoModificarEmergenteController implements Initializable {

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfApellidoP;

    @FXML
    private TextField tfApellidoM;

    @FXML
    private DatePicker dpFechaN;

    @FXML
    private TextField tfTel;

    @FXML
    private TextField tfCel;

    @FXML
    private TextField tfCorreo;

    @FXML
    private DatePicker dpFechaP;

    @FXML
    private TextField tfColegiatura;

    @FXML
    private ChoiceBox<String> cbNivel;

    @FXML
    private ChoiceBox<String> cbGrupo;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ChoiceBox<String> cbFactura;

    @FXML
    private TextField tfRFC;

    @FXML
    private TextField tfNResponsable;

    @FXML
    private TextField tfPResponsable;

    @FXML
    private TextField tfMResponsable;

    private String niveles[] = { "A1", "A2", "B1", "B2", "C1", "C2" };
    private String factura[] = { "Si", "No" };

    BorderPane pane;

    String query = null;

    Connection connection = null;

    PreparedStatement prep = null;

    Alumno alumno = null;
    private int idalumno;

    private ArrayList<String> grupos = new ArrayList<String>();

    @FXML
    void cancelarModificar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/modificarAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }

    @FXML
    void guardarModificar(ActionEvent event) throws SQLException, IOException {

        if (validarVacio()) {
            query = "UPDATE alumno SET nombre = ?, apaterno = ?, amaterno = ?, telefono = ?, celular = ?, correo = ?, fecha_nac = ?, fecha_pago = ?, colegiatura = ?, idnivel = ?, nombre_res = ?, apaterno_res = ?, amaterno_res = ?, RFC = ?, factura = ? WHERE idalumno = ?";
            prep = connection.prepareStatement(query);
            prep.setString(1, tfNombre.getText());
            prep.setString(2, tfApellidoP.getText());
            prep.setString(3, tfApellidoM.getText());
            prep.setString(4, tfTel.getText());
            prep.setString(5, tfCel.getText());
            prep.setString(6, tfCorreo.getText());
            prep.setDate(7, java.sql.Date.valueOf(dpFechaN.getValue().toString()));
            prep.setDate(8, java.sql.Date.valueOf(dpFechaP.getValue().toString()));
            prep.setInt(9, Integer.parseInt(tfColegiatura.getText()));
            prep.setInt(10, nivel());
            prep.setString(11, tfNResponsable.getText());
            prep.setString(12, tfPResponsable.getText());
            prep.setString(13, tfMResponsable.getText());
            prep.setString(14, tfRFC.getText());
            prep.setString(15, cbFactura.getValue());
            prep.setInt(16, idalumno);
            int salio = prep.executeUpdate();
            actualizarAlumnoGrupo();
            if (salio > 0)
                mensajeExito("Cambio de datos exitosamente");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/modificarAlumno.fxml"));
            Parent root = loader.load();
            pane = (BorderPane) myAnchor.getParent();
            pane.setCenter(root);
        }
    }

    private boolean validarVacio() {
        String valTel = "^[0-9]{7}$";
        String valCel = "^[0-9]{10}$";
        String valEmail = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        String valColeg = "^[0-9]+$";
        String valFecha = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
        String valName = "^[A-Za-zÀ-ú]+$";
        String valLast = "^[A-Za-zÀ-ú]*$";
        String valRfc = "^([A-ZÑ&]{3,4}) ?(?:- ?)?(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])) ?(?:- ?)?([A-Z\\d]{2})([A\\d])$";
        if (tfNombre.getText().trim().isEmpty() || !tfNombre.getText().trim().matches(valName)) {
            mensajeError("Nombre: Campo vacío o datos inválidos");
            return false;
        } else if (tfApellidoP.getText().trim().isEmpty() || !tfApellidoP.getText().trim().matches(valName)) {
            mensajeError("Apellido Paterno: Campo vacío o datos inválidos");
            return false;
        } else if (!tfApellidoM.getText().trim().matches(valLast)) {
            mensajeError("Apellido Materno: datos inválidos");
            return false;
        } else if (dpFechaN.getValue() == null || dpFechaN.getValue().toString().trim().isEmpty()
                || !dpFechaN.getValue().toString().matches(valFecha)) {
            mensajeError("Fecha de Nacimiento incorrecta");
            return false;
        } else if (!tfTel.getText().trim().matches(valTel)) {
            mensajeError("El número de telefono no posee 7 digitos");
            return false;
        } else if (!tfCel.getText().trim().matches(valCel)) {
            mensajeError("El número de celular no posee 10 digitos");
            return false;
        } else if (!tfCorreo.getText().trim().matches(valEmail)) {
            mensajeError("Correo inválido");
            return false;
        } else if (!tfColegiatura.getText().trim().matches(valColeg)) {
            mensajeError("Colegiatura debe poseer al menos un número");
            return false;
        } else if (dpFechaP.getValue() == null || dpFechaP.getValue().toString().trim().isEmpty()
                || !dpFechaP.getValue().toString().matches(valFecha)) {
            mensajeError("Fecha de Pago incorrecta");
            return false;
        } else if (cbNivel.getValue() == null) {
            mensajeError("Elige un nivel");
            return false;
        } else if (cbGrupo.getValue() == null) {
            mensajeError("Elige un grupo");
            return false;
        } else if (cbFactura.getValue() == "Si") {

            if (!tfRFC.getText().trim().matches(valRfc)) {
                mensajeError("RFC inválido");
                return false;
            } else if (tfNResponsable.getText().trim().isEmpty() || !tfNResponsable.getText().trim().matches(valName)) {
                mensajeError("Nombre del responsable: Campo vacío o datos inválidos");
                return false;
            } else if (tfPResponsable.getText().trim().isEmpty() || !tfPResponsable.getText().trim().matches(valName)) {
                mensajeError("Apellido Paterno del resposanble: Campo vacío o datos inválidos");
                return false;
            } else if (!tfMResponsable.getText().trim().matches(valLast)) {
                mensajeError("Apellido Materno del resposanble: datos inválidos");
                return false;
            }
        }
            return true;
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
        alert.setTitle("Exitoso");
        alert.setContentText(exito);
        alert.showAndWait();
    }

    public void setDatos(int idAlumno, String nombre, String paterno, String materno, String telefono, String celular,
            String correo, Date fechaNac, Date fechaPago, int colegiatura, int nivel, int idgrupo, String grupo,
            String nombreResponsable, String aPaternoResponsable, String aMaternoResponsable, String rfc,
            String factura) {

        this.tfNombre.setText(nombre);
        this.tfApellidoP.setText(paterno);
        this.tfApellidoM.setText(materno);
        this.tfTel.setText(telefono);
        this.tfCel.setText(celular);
        this.tfCorreo.setText(correo);
        this.tfColegiatura.setText(String.valueOf(colegiatura));
        String lvl = nivelE(nivel);
        this.cbNivel.setValue(lvl);
        this.dpFechaN.setValue(fechaNac.toLocalDate());
        this.dpFechaP.setValue(fechaPago.toLocalDate());
        this.idalumno = idAlumno;
        this.cbGrupo.setValue(grupo);
        this.cbFactura.setValue(factura);
        this.tfNResponsable.setText(nombreResponsable);
        this.tfPResponsable.setText(aPaternoResponsable);
        this.tfMResponsable.setText(aMaternoResponsable);
        this.tfRFC.setText(rfc);

    }

    private String nivelE(int x) {

        String lvl = "";

        switch (x) {

            case 1:
                lvl = "A1";
                break;
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

    private int nivel() {
        String nivel = cbNivel.getValue();
        int lvl = 0;

        switch (nivel) {

            case "A1":
                lvl = 1;
                break;
            case "A2":
                lvl = 2;
                break;
            case "B1":
                lvl = 3;
                break;
            case "B2":
                lvl = 4;
                break;
            case "C1":
                lvl = 5;
                break;
            case "C2":
                lvl = 6;
                break;

        }
        return lvl;
    }

    private void obtenerGrupos() throws SQLException {
        grupos.clear();
        cbGrupo.getItems().clear();
        ResultSet res = null;
        String query = "SELECT codigo_grupo FROM Grupo INNER JOIN nivel ON Grupo.idnivel = nivel.idnivel WHERE codigo_nivel = ?";
        prep = connection.prepareStatement(query);
        prep.setString(1, cbNivel.getValue());
        res = prep.executeQuery();

        while (res.next()) {
            grupos.add(res.getString("codigo_grupo"));
        }

        cbGrupo.getItems().addAll(grupos);

    }

    private void actualizarAlumnoGrupo() throws SQLException {
        PreparedStatement prep2;
        String insertQuery = "UPDATE Grupo_Alumnos SET idgrupo = ? WHERE idalumno = ?";
        prep2 = connection.prepareStatement(insertQuery);
        prep2.setInt(1, obtenerIdGrupo());
        prep2.setInt(2, idalumno);
        prep2.executeUpdate();

    }

    private int obtenerIdGrupo() throws SQLException {
        ResultSet res = null;
        int grupo = 0;
        PreparedStatement prep3;
        String query = "SELECT idgrupo FROM Grupo WHERE codigo_grupo = ?";
        prep3 = connection.prepareStatement(query);
        prep3.setString(1, cbGrupo.getValue());
        res = prep3.executeQuery();
        while (res.next()) {
            grupo = res.getInt("idgrupo");
        }
        return grupo;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFechaN.getEditor().setDisable(true);
        dpFechaP.getEditor().setDisable(true);
        cbNivel.getItems().addAll(niveles);
        cbNivel.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            try {
                obtenerGrupos();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        cbFactura.getItems().addAll(factura);
        cbFactura.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == "No") {
                tfRFC.setDisable(true);
                tfNResponsable.setDisable(true);
                tfPResponsable.setDisable(true);
                tfMResponsable.setDisable(true);
                this.tfNResponsable.setText(null);
                this.tfPResponsable.setText(null);
                this.tfMResponsable.setText(null);
                this.tfRFC.setText(null);
            } else {
                tfRFC.setDisable(false);
                tfNResponsable.setDisable(false);
                tfPResponsable.setDisable(false);
                tfMResponsable.setDisable(false);
            }
        });
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
