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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Grupos;

public class ModificarGrupoController implements Initializable {

    @FXML
    private TableColumn<Grupos, Grupos> icono;

    @FXML
    private TableColumn<?, ?> id_grupo;

    @FXML
    private TableColumn<?, ?> codigo_grupo;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TableView<Grupos> tablaGrupos;

    @FXML
    private TableColumn<?, ?> nombre_grupo;

    @FXML
    private TableColumn<?, ?> nivel;

    @FXML
    private TextField txtfilterField;

    @FXML
    private ChoiceBox<String> cbFiltro;

    private String opciones [] = {"ID_GRUPO", "CODIGO_GRUPO", "NOMBRE", "NIVEL", "TODOS"};
    @FXML


    BorderPane pane;

    @FXML
    void salir(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GrupoBorder.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();

        pane.setCenter(root);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        icono.setStyle("-fx-alignment: CENTER;");
        cbFiltro.getItems().setAll(opciones);
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
    Grupos grupo = null;

    private void cargarDatos() throws SQLException {

        connection = Conector.conectar();
        refrescar();
        id_grupo.setCellValueFactory(new PropertyValueFactory<>("idgrupo"));
        codigo_grupo.setCellValueFactory(new PropertyValueFactory<>("codigoGrupo"));
        nombre_grupo.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));
        nivel.setCellValueFactory( new PropertyValueFactory<>("idnivel"));
        
        icono.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );

        icono.setCellFactory(param -> new TableCell<Grupos, Grupos>() {
            private final Button deleteButton = new Button("Modificar");

            @Override
            protected void updateItem(Grupos grupoo, boolean empty) {
                super.updateItem(grupoo, empty);

                if (grupoo == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    (event) -> {
                        try {

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ModificarGrupoEmergente.fxml"));
                            Parent root;
                            root = loader.load();
                            pane = (BorderPane) myAnchor.getParent();

                            ModificarGrupoEmergente2 obj = loader.getController();
                            obj.setDatos(grupoo.getNombreGrupo(), grupoo.getIdnivel(), grupoo.getCodigoGrupo(), grupoo.getIdgrupo());

                            pane.setCenter(root);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                    }
                );
            }
        });
    }

    
    ObservableList <Grupos> ListaGrupos = FXCollections.observableArrayList();

    public void refrescar() throws SQLException{

        ListaGrupos.clear();
        query = "SELECT * FROM grupo";
        prep = connection.prepareStatement(query);
        res = prep.executeQuery();

        while(res.next()){

            ListaGrupos.add( new Grupos(res.getInt("idgrupo"), res.getString("codigo_grupo"), res.getString("nombre_grupo"), res.getInt("idnivel")));
        }

        tablaGrupos.setItems(ListaGrupos);
        filtrar();
    }

    private void filtrar() {
        FilteredList<Grupos> filteredData = new FilteredList<>(ListaGrupos, b -> true);

        txtfilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate( grupo -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();

                if(cbFiltro.getValue().equals("TODOS") ){

                    if(String.valueOf(grupo.getIdgrupo()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(grupo.getNombreGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(grupo.getCodigoGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if( String.valueOf(grupo.getIdnivel()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;

                }
                else{

                    if(cbFiltro.getValue().equals("ID_GRUPO") && String.valueOf(grupo.getIdgrupo()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("NOMBRE") && grupo.getNombreGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("CODIGO_GRUPO") && grupo.getCodigoGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFiltro.getValue().equals("NIVEL") && String.valueOf(grupo.getIdnivel()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
            
                    else return false;
            }
            });


        });

        SortedList<Grupos> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaGrupos.comparatorProperty());
        tablaGrupos.setItems(sortedData); 
        
    }
}
