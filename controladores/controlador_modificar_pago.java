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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
// import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Pago;

public class controlador_modificar_pago implements Initializable {

    @FXML
    private ChoiceBox<String> cbFilter;

    @FXML
    private TextField filterField;

    @FXML
    private Button btn_salir;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TableColumn<?, ?> colum_Cantidad;

    @FXML
    private TableColumn<?, ?> colum_IdAlum;

    @FXML
    private TableColumn<?, ?> colum_Id;

    @FXML
    private TableColumn<Pago, Pago> colum_Accion;


    @FXML
    private TableColumn<?, ?> colum_Fecha;

    @FXML
    private TableColumn<?, ?> colum_Concepto;

    @FXML
    private TableView <Pago> tablaPagos;

    private BorderPane pane;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_menu_pagos.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }

    private String opciones [] = {"Todos", "Id Pago","Id Alumno", "Cantidad", "Concepto", "Fecha"};
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbFilter.getItems().addAll(opciones);
        cbFilter.setValue("Todos");

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
    Pago pago = null;

    ObservableList <Pago> ListaPagos = FXCollections.observableArrayList();

    private void cargarDatos() throws SQLException {
        connection = Conector.conectar();
        refrescar();
        
        colum_Id.setCellValueFactory(new PropertyValueFactory<>("idPagos")); 
        colum_Cantidad.setCellValueFactory(new PropertyValueFactory<>("monto")); 
        colum_Fecha.setCellValueFactory(new PropertyValueFactory<>("fecha")); 
        colum_Concepto.setCellValueFactory(new PropertyValueFactory<>("concepto")); 
        colum_IdAlum.setCellValueFactory(new PropertyValueFactory<>("idAlumno")); 
        
        colum_Accion.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );

        colum_Accion.setCellFactory(param -> new TableCell<Pago, Pago>() {
            private final Button deleteButton = new Button("Modificar");

            @Override
            protected void updateItem(Pago pago, boolean empty) {
                super.updateItem(pago, empty);

                if (pago == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    (event) -> {

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ventana_modificar_pago.fxml"));
                            Parent root;
                            root = loader.load();
                            pane = (BorderPane) myAnchor.getParent();

                            controlador_ventana_modificar_pago obj = loader.getController();
                            obj.setDatos(pago.getIdPagos(), Integer.parseInt(pago.getMonto()), pago.getFecha() , pago.getConcepto(), pago.getIdAlumno());
                            
                            
                            pane.setCenter(root);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                );
            }
        });
    }

    

    private void refrescar() throws SQLException {
        ListaPagos.clear();
        query = "SELECT * FROM pagos_realizados";
        prep = connection.prepareStatement(query);
        res = prep.executeQuery();

        while(res.next()){

            ListaPagos.add( new Pago(res.getInt("idpagos"), res.getString("monto"), res.getDate("fecha"), res.getString("concepto"), res.getInt("idalumno")));
        }

        tablaPagos.setItems(ListaPagos);

         filtrar();
    }

    private void filtrar(){
        FilteredList<Pago> filteredData = new FilteredList<>(ListaPagos, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pago -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();

                if(cbFilter.getValue().equals("Todos") ){
                    if(String.valueOf(pago.getIdPagos()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(pago.getConcepto().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(String.valueOf(pago.getIdAlumno()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if( pago.getMonto().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;
                }
                else{    
                
                    if(cbFilter.getValue().equals("Id Pago") && String.valueOf(pago.getIdPagos()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("Id Alumno") && String.valueOf(pago.getIdAlumno()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("Cantidad") && pago.getMonto().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("Concepto") && pago.getConcepto().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;
            }
            });


        });

        SortedList<Pago> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaPagos.comparatorProperty());
        tablaPagos.setItems(sortedData);
    }

}
