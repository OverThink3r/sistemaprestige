package controladores;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class PagosController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btn_consultar_adeudo;

    @FXML
    private Button btn_consultar_pago;

    @FXML
    private Button btn_eliminar_pago;

    @FXML
    private Button btn_mostrar_pago;

    @FXML
    private Button btn_registrar_pago;

    @FXML
    void RegistrarPago(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_registrar_pago.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void ModificarPago(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_modificar_pago.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void ConsultarPago(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_consultar_pago.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void EliminarPago(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_eliminar_pago.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }

    @FXML
    void ConsultarAdeudo(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_consultar_adeudo.fxml"));
        Parent root = loader.load();
        borderPane.setCenter(root);
    }
}
