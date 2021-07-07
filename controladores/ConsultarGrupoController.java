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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.EncabezadosExcel;
import objetos.Grupos;

public class ConsultarGrupoController implements Initializable{
    @FXML
    private TableColumn<Grupos, Grupos> icono;

    @FXML
    private TableColumn<?, ?> id_grupo;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TableColumn<?, ?> codigo_grupo;

    @FXML
    private TableColumn<?, ?> nombre_grupo;

    @FXML
    private TableColumn<?, ?> nivel;

    @FXML
    private TableView<Grupos> tablaGrupos;

    @FXML
    private ChoiceBox<String> cbFilter;

    private String opciones [] = {"ID_GRUPO", "CODIGO_GRUPO", "NOMBRE", "NIVEL", "TODOS"};

    @FXML
    private TextField txtFilter;

    private BorderPane pane;

    @FXML
    void salir(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/GrupoBorder.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();

        pane.setCenter(root);
    }

    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbFilter.getItems().addAll(opciones);
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

    ObservableList <Grupos> ListaGrupos = FXCollections.observableArrayList();

    private void cargarDatos() throws SQLException {

        connection = Conector.conectar();
        refrescar();
        id_grupo.setCellValueFactory(new PropertyValueFactory<>("idgrupo"));
        codigo_grupo.setCellValueFactory(new PropertyValueFactory<>("codigoGrupo"));
        nombre_grupo.setCellValueFactory(new PropertyValueFactory<>("nombreGrupo"));
        nivel.setCellValueFactory( new PropertyValueFactory<>("idnivel"));

    }

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

        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate( grupo -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();

                if(cbFilter.getValue().equals("TODOS") ){

                    if(String.valueOf(grupo.getIdgrupo()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(grupo.getNombreGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(grupo.getCodigoGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if( String.valueOf(grupo.getIdnivel()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;

                }
                else{
                    if(cbFilter.getValue().equals("ID_GRUPO") && String.valueOf(grupo.getIdgrupo()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("NOMBRE") && grupo.getNombreGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("CODIGO_GRUPO") && grupo.getCodigoGrupo().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else if(cbFilter.getValue().equals("NIVEL") && String.valueOf(grupo.getIdnivel()).toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;
            }
            });


        });

        SortedList<Grupos> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaGrupos.comparatorProperty());
        tablaGrupos.setItems(sortedData);       
    }

    @FXML
    void generarPdf(ActionEvent event) throws FileNotFoundException, DocumentException {
        Document documento = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/reporteGrupos.pdf"));
        documento.open();
        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell("ID GRUPO");
        tabla.addCell("CODIGO GRUPO");
        tabla.addCell("NOMBRE GRUPO");
        tabla.addCell("ID NIVEL");

        for (int i = 0; i < tablaGrupos.getItems().size(); i++){
             
            tabla.addCell(tablaGrupos.getItems().get(i).getNombreGrupo());  
            tabla.addCell(tablaGrupos.getItems().get(i).getCodigoGrupo());
            tabla.addCell(String.valueOf(tablaGrupos.getItems().get(i).getIdgrupo()));
            tabla.addCell(String.valueOf(tablaGrupos.getItems().get(i).getIdnivel()));             
        } 
        documento.add(tabla);
        documento.close();
    }

    @FXML
    void generarExcel(ActionEvent event) throws IOException {
        String ruta = System.getProperty("user.home");
        String excelFilePath = ruta + "/Desktop/reporteGrupos.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CellStyle cs = workbook.createCellStyle();
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.LEFT);
        EncabezadosExcel header = new EncabezadosExcel();
        
        header.createHeaderRow(sheet, "GRUPOS");
        int rowCount = 0;

        for (int i = 0; i < tablaGrupos.getItems().size(); i++) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(tablaGrupos.getItems().get(i).getIdgrupo());
            cell1.setCellStyle(cs);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(tablaGrupos.getItems().get(i).getCodigoGrupo());
            cell2.setCellStyle(cs);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(tablaGrupos.getItems().get(i).getNombreGrupo());
            cell3.setCellStyle(cs);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(tablaGrupos.getItems().get(i).getIdnivel());
            cell4.setCellStyle(cs);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
            workbook.close();
        }

    }
}
