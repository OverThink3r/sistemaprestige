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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
import objetos.Usuario;


public class vistaConsultarUsuarioController implements Initializable {

    @FXML
    private Button btnSalir;

    @FXML
    private TableColumn<?, ?> Id_Usuario;


    @FXML
    private TableColumn<?, ?> apMaterno;

    @FXML
    private TableColumn<?, ?> nombreUser;

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
    
    BorderPane pane;
    
    @FXML
    private AnchorPane myAnchor;

    @FXML
    void onbtnSalirClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/vistaUsuario.fxml"));
        Parent root = loader.load();
        
        pane = (BorderPane) myAnchor.getParent();

        pane.setCenter(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        nombreUser.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario")); 
        rol.setCellValueFactory(new PropertyValueFactory<>("rol")); 
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre")); 
        apPaterno.setCellValueFactory(new PropertyValueFactory<>("Paterno")); 
        apMaterno.setCellValueFactory(new PropertyValueFactory<>("Materno")); 
        
        
    }

    @FXML
    void generarPdf(ActionEvent event) throws FileNotFoundException, DocumentException {
        
        Document documento = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/reporteUsuarios.pdf"));
        documento.open();
        PdfPTable tabla = new PdfPTable(5);
        tabla.addCell("NOMBRE");
        tabla.addCell("APELLIDO PATERNO");
        tabla.addCell("APELLIDO MATERNO");
        tabla.addCell("NOMBRE DE USUARIO");
        tabla.addCell("ROL");

        for (int i = 0; i < tablaUsuarios.getItems().size(); i++){
              
            tabla.addCell(tablaUsuarios.getItems().get(i).getNombre());
            tabla.addCell(tablaUsuarios.getItems().get(i).getPaterno());
            tabla.addCell(tablaUsuarios.getItems().get(i).getMaterno());  
            tabla.addCell(tablaUsuarios.getItems().get(i).getNombreUsuario());           
            tabla.addCell(tablaUsuarios.getItems().get(i).getRol());  
        } 
        documento.add(tabla);
        documento.close();

    }

    @FXML
    void generarExcel(ActionEvent event) throws IOException {
        String ruta = System.getProperty("user.home");
        String excelFilePath = ruta + "/Desktop/reporteUsuarios.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CellStyle cs = workbook.createCellStyle();
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.LEFT);
        EncabezadosExcel header = new EncabezadosExcel();


        
        header.createHeaderRow(sheet, "USUARIOS");
        int rowCount = 0;

        for (int i = 0; i < tablaUsuarios.getItems().size(); i++) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(tablaUsuarios.getItems().get(i).getNombre());
            cell1.setCellStyle(cs);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(tablaUsuarios.getItems().get(i).getPaterno());
            cell2.setCellStyle(cs);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(tablaUsuarios.getItems().get(i).getMaterno());
            cell3.setCellStyle(cs);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(tablaUsuarios.getItems().get(i).getNombreUsuario());
            cell4.setCellStyle(cs);
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(tablaUsuarios.getItems().get(i).getRol());
            cell5.setCellStyle(cs);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
            workbook.close();
        }
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
                    else if(cbFiltro.getValue().equals("Rol") && usuario.getRol().toLowerCase().indexOf(lowerCaseFilter)!=-1) return true;
                    else return false;
            }
             });


        });

        SortedList<Usuario> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaUsuarios.comparatorProperty());
        tablaUsuarios.setItems(sortedData); 
    }



}
