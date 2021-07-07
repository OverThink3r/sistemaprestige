package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Usuario;

public class vistaEliminarController implements Initializable {
    
    @FXML
    private Button btnSalir;

    @FXML
    private TableColumn<?, ?> Id_Usuario;

    @FXML
    private TableColumn<?, ?> usuario;

    @FXML
    private TableColumn<Usuario, Usuario> icono;

    @FXML
    private TableColumn<?, ?> apMaterno;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TableColumn<?, ?> nombre;

    @FXML
    private TableColumn<?, ?> apPaterno;

    @FXML
    private TableColumn<?, ?> rol;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TextField txtFiltro;
    @FXML
    private ChoiceBox<String> cbFiltro;
   
    private BorderPane pane;
    
    @FXML
    void onbtnSalirClick(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaUsuario.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();

        pane.setCenter(root);
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        icono.setStyle("-fx-alignment: CENTER;");
        cbFiltro.getItems().addAll(opciones);
        cbFiltro.setValue("Todos");
        try {
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String query = null;
    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet res = null;
    Usuario usuarioo = null;

    ObservableList <Usuario> ListaUsuarios = FXCollections.observableArrayList();
     private String opciones [] = {"Todos", "Id Usuario","Nombre", "Apellido paterno", "Apellido materno", "Nombre de usuario", "Rol"};

    private void cargarDatos() throws SQLException {
        connection = Conector.conectar();
        refrescar();
        
        Id_Usuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario")); 
        usuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario")); 
        rol.setCellValueFactory(new PropertyValueFactory<>("rol")); 
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre")); 
        apPaterno.setCellValueFactory(new PropertyValueFactory<>("Paterno")); 
        apMaterno.setCellValueFactory(new PropertyValueFactory<>("Materno")); 
        
        icono.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );

        icono.setCellFactory(param -> new TableCell<Usuario, Usuario>() {
            private final Button deleteButton = new Button("Eliminar");

            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);

                if (usuario == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    (event) -> {
                        ListaUsuarios.remove(usuario);

                        try {
                            
                            prep = connection.prepareStatement("DELETE FROM usuario WHERE idusuario = ?");
                            
                            prep.setInt(1, usuario.getIdUsuario());
                            int salio = prep.executeUpdate();

                            if(salio > 0) mensajeExito("Eliminado Correctamente");
                            else mensajeError("No se pudo eliminar");

                            refrescar();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                );
            }
        });
        
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
    

    private void refrescar() throws SQLException {
        ListaUsuarios.clear();
        query = "SELECT * FROM usuario";
        prep = connection.prepareStatement(query);
        res = prep.executeQuery();

        while(res.next()){

            ListaUsuarios.add( new Usuario(res.getInt("idusuario"),res.getString("nombre_usuario"), res.getString("rol"), res.getString("nombre"), res.getString("apaterno"), res.getString("amaterno")));
        }

        tablaUsuarios.setItems(ListaUsuarios);
        filtrar();
    }

    private void filtrar(){
        FilteredList<Usuario> filteredData = new FilteredList<>(ListaUsuarios, b -> true);
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(usuario -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();

                if(cbFiltro.getValue().equals("Todos") ){
                    if(String.valueOf(usuario.getIdUsuario()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(usuario.getNombre().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(usuario.getPaterno().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(usuario.getMaterno().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(usuario.getNombreUsuario().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(usuario.getRol().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                   
                    else return false;
                }
                else{    
               
                    if(cbFiltro.getValue().equals("Id Usuario") && String.valueOf(usuario.getIdUsuario()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("Nombre") && usuario.getNombre().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("Apellido paterno") && usuario.getPaterno().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("Apellido materno") && usuario.getMaterno().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("Nombre de usuario") && usuario.getNombreUsuario().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("Rol") && usuario.getRol().toLowerCase().indexOf(lowerCaseFilter)!=-1)  return true;
                    else return false;
            }
             });


        });

        SortedList<Usuario> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaUsuarios.comparatorProperty());
        tablaUsuarios.setItems(sortedData); 
    }

}
