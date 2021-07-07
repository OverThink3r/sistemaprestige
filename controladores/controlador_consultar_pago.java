package controladores;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;


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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.EncabezadosExcel;
import objetos.Pago;

public class controlador_consultar_pago implements Initializable {


@FXML
private ChoiceBox<String> cbFilter;

@FXML
private TextField filterField;

  @FXML
  private Button btn_salir;

  @FXML
  private TableColumn<?, ?> colum_Cantidad;

  @FXML
  private TableColumn<?, ?> colum_IdAlum;

  @FXML
  private TableColumn<?, ?> colum_Id;

  @FXML
  private AnchorPane myAnchor;

  @FXML
  private TableColumn<?, ?> colum_Fecha;

  @FXML
  private TableColumn<?, ?> colum_accion;

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
        // Deben ser las variables de su clase objeto
        colum_Id.setCellValueFactory(new PropertyValueFactory<>("idPagos")); 
        colum_Cantidad.setCellValueFactory(new PropertyValueFactory<>("monto")); 
        colum_Fecha.setCellValueFactory(new PropertyValueFactory<>("fecha")); 
        colum_Concepto.setCellValueFactory(new PropertyValueFactory<>("concepto")); 
        colum_IdAlum.setCellValueFactory(new PropertyValueFactory<>("idAlumno")); 
        
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

    @FXML
    void generarPdf(ActionEvent event) throws FileNotFoundException, DocumentException {
        Document documento = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/reportePagos.pdf"));
        documento.open();
        PdfPTable tabla = new PdfPTable(5);
        tabla.addCell("Id Pago");
        tabla.addCell("Cantidad");
        tabla.addCell("Fecha");
        tabla.addCell("Concepto");
        tabla.addCell("Id Alumno");
       
        for (int i = 0; i < tablaPagos.getItems().size(); i++){

            tabla.addCell(String.valueOf(tablaPagos.getItems().get(i).getIdPagos()));
            tabla.addCell(tablaPagos.getItems().get(i).getMonto());
            tabla.addCell(String.valueOf(tablaPagos.getItems().get(i).getFecha()));
            tabla.addCell(tablaPagos.getItems().get(i).getConcepto());
            tabla.addCell(String.valueOf(tablaPagos.getItems().get(i).getIdAlumno()));
        }

        documento.add(tabla);
        documento.close();
        
    }

    @FXML
    void generarExcel(ActionEvent event) throws IOException {

         String ruta = System.getProperty("user.home");
        String excelFilePath = ruta + "/Desktop/reportePagos.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CellStyle cs = workbook.createCellStyle();
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.LEFT);
        EncabezadosExcel header = new EncabezadosExcel();
        
        header.createHeaderRow(sheet, "PAGOS");
        int rowCount = 0;

        for (int i = 0; i < tablaPagos.getItems().size(); i++) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(tablaPagos.getItems().get(i).getIdPagos());
            cell1.setCellStyle(cs);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(tablaPagos.getItems().get(i).getMonto());
            cell2.setCellStyle(cs);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue( String.valueOf(tablaPagos.getItems().get(i).getFecha()));
            cell3.setCellStyle(cs);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(tablaPagos.getItems().get(i).getConcepto());
            cell4.setCellStyle(cs);
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(tablaPagos.getItems().get(i).getIdAlumno());
            cell5.setCellStyle(cs);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
            workbook.close();
        }

    }
    }


