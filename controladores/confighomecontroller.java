package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import modelos.Conector;
import objetos.User;
import objetos.UserHolder;
import objetos.encrypt;
import javafx.scene.Node;

public class confighomecontroller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private BorderPane bphconfig;

    @FXML
    private ChoiceBox<String> ques1;

    @FXML
    private ChoiceBox<String> ques2;

    @FXML
    private TextField ans2;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField rpass;

    @FXML
    private TextField ans1;

    @FXML
    private TextField usern;

    @FXML
    private TextField secn2;

    @FXML
    private TextField secn1;

    @FXML
    private TextField names;

    Connection connection;
    PreparedStatement prep;
    ResultSet res;

    UserHolder holder = UserHolder.getInstance();
    User u = holder.getUser();
    String uname = u.getName();

    private ArrayList<String> qsl = new ArrayList<String>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT preguntascol FROM preguntas;";

        try {
            prep = connection.prepareStatement(query);
            res = prep.executeQuery();
            while (res.next()) {
                qsl.add(res.getString("preguntascol"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ques1.getItems().addAll(qsl);
        ques2.getItems().addAll(qsl);
        setInfo();
        setvalues();
    }

    public void setInfo() {
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT nombre_usuario, R_1, R_2, nombre, apaterno, amaterno FROM usuario WHERE nombre_usuario = ?";
        try {
            prep = connection.prepareStatement(query);
            prep.setString(1, uname);
            res = prep.executeQuery();
            res.next();
            usern.setText(res.getString("nombre_usuario"));
            ans1.setText(res.getString("R_1"));
            ans2.setText(res.getString("R_2"));
            names.setText(res.getString("nombre"));
            secn1.setText(res.getString("apaterno"));
            secn2.setText(res.getString("amaterno"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setvalues() {
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query1 = "SELECT preguntas.preguntascol FROM preguntas INNER JOIN  usuario ON usuario.idpreguntas_1 = preguntas.idpreguntas WHERE usuario.nombre_usuario = '"
                + uname + "'";
        String query2 = "SELECT preguntas.preguntascol FROM preguntas INNER JOIN  usuario ON usuario.idpreguntas_2 = preguntas.idpreguntas WHERE usuario.nombre_usuario = '"
                + uname + "'";
        try {
            prep = connection.prepareStatement(query1);
            res = prep.executeQuery();
            res.next();
            ques1.setValue(res.getString("preguntascol"));
            prep = connection.prepareStatement(query2);
            res = prep.executeQuery();
            res.next();
            ques2.setValue(res.getString("preguntascol"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void safeconfg(ActionEvent event) throws IOException {
        String ns = names.getText().trim();
        String sc1 = secn1.getText().trim();
        String sc2 = secn2.getText().trim();
        String un = usern.getText().trim();
        String a1 = ans1.getText().trim();
        String a2 = ans2.getText().trim();
        String ps = pass.getText().trim();
        String rps = rpass.getText().trim();

        if (ques1.getValue() != null && ques1.getValue() != null && ns != null && !ns.isEmpty() && !ns.trim().isEmpty()
                && sc1 != null && !sc1.isEmpty() && !sc1.trim().isEmpty() && sc2 != null && !sc2.isEmpty()
                && !sc2.trim().isEmpty() && un != null && !un.isEmpty() && !un.trim().isEmpty() && a1 != null
                && !a1.isEmpty() && !a1.trim().isEmpty() && a2 != null && !a2.isEmpty() && !a2.trim().isEmpty()
                && ps != null && !ps.isEmpty() && !ps.trim().isEmpty() && rps != null && !rps.isEmpty()
                && !rps.trim().isEmpty()) {
            if (!ques1.getValue().equals(ques2.getValue())) {
                if (ps.equals(rps)) {
                    try {
                        connection = Conector.conectar();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String query = "SELECT contraseña FROM usuario WHERE nombre_usuario = '" + uname + "'";
                    try {
                        prep = connection.prepareStatement(query);
                        res = prep.executeQuery();
                        res.next();
                        byte[] combined = res.getBytes("contraseña");
                        encrypt enc = new encrypt();
                        if (enc.verifypass(combined, ps)) {
                            try {
                                connection = Conector.conectar();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            String query2 = "UPDATE usuario SET nombre_usuario ='" + un + "', R_1 = '" + a1
                                    + "', R_2 = '" + a2 + "', idpreguntas_1 = '" + idquest(ques1.getValue())
                                    + "', idpreguntas_2 = '" + idquest(ques2.getValue()) + "', nombre = '" + ns
                                    + "', apaterno = '" + sc1 + "', amaterno = '" + sc2 + "' WHERE nombre_usuario = '"
                                    + uname + "'";

                            try {
                                prep = connection.prepareStatement(query2);
                                int ef = prep.executeUpdate();
                                if (ef == 1) {
                                    Alert alrt = new Alert(Alert.AlertType.INFORMATION);
                                    alrt.setTitle("Informacion");
                                    alrt.setContentText("Sera redirigido a la ventana de inicio");
                                    alrt.setHeaderText("Sus datos han sido modificados exitosamente");
                                    alrt.show();

                                    root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
                                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                                    stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
                                    stage.setY((bounds.getHeight() - stage.getHeight()) / 4);
                                } else {
                                    Alert ale = new Alert(Alert.AlertType.ERROR);
                                    ale.setTitle("Error");
                                    ale.setContentText("No se pudo cambiar su contraseña");
                                    ale.setHeaderText(null);
                                    ale.show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert al = new Alert(Alert.AlertType.ERROR);
                            al.setTitle("Error");
                            al.setContentText("Contraseña incorrecta");
                            al.setHeaderText(null);
                            al.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                al.setContentText("Las preguntas no pueden ser iguales");
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

    public int idquest(String quescol) {
        int id = 0;
        try {
            connection = Conector.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT idpreguntas FROM preguntas WHERE preguntascol = '" + quescol + "'";

        try {
            prep = connection.prepareStatement(query);
            res = prep.executeQuery();
            res.next();
            id = res.getInt("idpreguntas");
            return id;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
