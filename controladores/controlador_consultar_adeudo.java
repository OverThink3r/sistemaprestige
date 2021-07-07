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
import objetos.Adeudo;
import objetos.EncabezadosExcel;

public class controlador_consultar_adeudo implements Initializable {

     @FXML
    private ChoiceBox<String> cbFilter;

    @FXML
    private TextField filterField; 

    @FXML
    private Button btn_salir;

    @FXML
    private TableColumn<?, ?> colum_Cantidad;

    @FXML
    private TableColumn<?, ?> colum_Pagado;

    @FXML
    private TableColumn<?, ?> colum_Id;

    @FXML
    private TableColumn<?, ?> colum_Accion;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TableColumn<?, ?> colum_Fecha;

    @FXML
    private TableColumn<?, ?> Colum_IdAlum;

    @FXML
    private TableView<Adeudo> tablaAdeudos;

    private BorderPane pane;
    
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vista_menu_pagos.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
         pane.setCenter(root);
    }

 private String opciones [] = {"Todos", "Id Pago","Id Alumno", "Cantidad", "Pagado", "Fecha"};

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
    Adeudo adeudo = null;

    ObservableList <Adeudo> listaAdeudos = FXCollections.observableArrayList();

    private void cargarDatos() throws SQLException {
        connection = Conector.conectar();
        refrescar();
        // Deben ser las variables de su clase objeto
        colum_Id.setCellValueFactory(new PropertyValueFactory<>("idAdeudos")); 
        colum_Cantidad.setCellValueFactory(new PropertyValueFactory<>("monto")); 
        Colum_IdAlum.setCellValueFactory(new PropertyValueFactory<>("idAlumno")); 
        colum_Fecha.setCellValueFactory(new PropertyValueFactory<>("mes")); 
        colum_Pagado.setCellValueFactory(new PropertyValueFactory<>("pagado")); 
            
    }

    private void refrescar() throws SQLException {
        listaAdeudos.clear();
        query = "SELECT * FROM adeudos";
        prep = connection.prepareStatement(query);
        res = prep.executeQuery();

        while(res.next()){
            listaAdeudos.add( new Adeudo(res.getInt("idadeudos"), res.getInt("monto"), res.getInt("idalumno"), res.getDate("mes"), res.getInt("pagado")) );
        }

        tablaAdeudos.setItems(listaAdeudos);
        filtrar();
    }

    private void filtrar(){
        FilteredList<Adeudo> filteredData = new FilteredList<>(listaAdeudos, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pago -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();

                if(cbFilter.getValue().equals("Todos") ){
                    if(String.valueOf(pago.getIdAdeudos()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                     else if(String.valueOf(pago.getMonto()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                     else if(String.valueOf(pago.getIdAlumno()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                      else if(String.valueOf(pago.getPagado()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;
                }
                else{    
                
                    if(cbFilter.getValue().equals("Id Pago") && String.valueOf(pago.getIdAdeudos()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("Cantidad") && String.valueOf(pago.getMonto()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("Id Alumno") && String.valueOf(pago.getIdAlumno()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("Pagado") && String.valueOf(pago.getPagado()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;
            }
            });


        });

        SortedList<Adeudo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaAdeudos.comparatorProperty());
        tablaAdeudos.setItems(sortedData);
    }

    @FXML
    void generarPdf(ActionEvent event) throws FileNotFoundException, DocumentException {
        Document documento = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/reporteAdeudos.pdf"));
        documento.open();
        PdfPTable tabla = new PdfPTable(5);
        tabla.addCell("Id Adeudo");
        tabla.addCell("Cantidad");
        tabla.addCell("Fecha");
        tabla.addCell("Pagado");
        tabla.addCell("Id Alumno");
       
        for (int i = 0; i < tablaAdeudos.getItems().size(); i++){

            tabla.addCell(String.valueOf(tablaAdeudos.getItems().get(i).getIdAdeudos()));
            tabla.addCell(String.valueOf(tablaAdeudos.getItems().get(i).getMonto()));
            tabla.addCell(String.valueOf(tablaAdeudos.getItems().get(i).getMes()));
            tabla.addCell(String.valueOf(tablaAdeudos.getItems().get(i).getPagado()));
            tabla.addCell(String.valueOf(tablaAdeudos.getItems().get(i).getIdAlumno()));
        }

        documento.add(tabla);
        documento.close();
        
    }
    @FXML
    void generarExcel(ActionEvent event) throws IOException {
        String ruta = System.getProperty("user.home");
        String excelFilePath = ruta + "/Desktop/reporteAdeudos.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CellStyle cs = workbook.createCellStyle();
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.LEFT);
        EncabezadosExcel header = new EncabezadosExcel();


        
        header.createHeaderRow(sheet, "ADEUDO");
        int rowCount = 0;

        for (int i = 0; i < tablaAdeudos.getItems().size(); i++) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(tablaAdeudos.getItems().get(i).getIdAdeudos());
            cell1.setCellStyle(cs);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(tablaAdeudos.getItems().get(i).getMonto());
            cell2.setCellStyle(cs);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(String.valueOf(tablaAdeudos.getItems().get(i).getMes()));
            cell3.setCellStyle(cs);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(tablaAdeudos.getItems().get(i).getPagado());
            cell4.setCellStyle(cs);
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(tablaAdeudos.getItems().get(i).getIdAlumno());
            cell5.setCellStyle(cs);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
            workbook.close();
        }
    }
}
