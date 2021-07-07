package controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Alumno;
import objetos.EncabezadosExcel;
import javafx.scene.Parent;


public class alumnoConsultarController implements Initializable {
    private BorderPane pane;

    @FXML
    private AnchorPane apConsultarAlumno;

    @FXML
    private TableView<Alumno> tbAlumno;

    @FXML
    private TableColumn<?, ?> idAlumno;

    @FXML
    private TableColumn<?, ?> nombre;

    @FXML
    private TableColumn<?, ?> apellidoP;

    @FXML
    private TableColumn<?, ?> apellidoM;

    @FXML
    private TableColumn<?, ?> fechaN;

    @FXML
    private TableColumn<?, ?> telefono;

    @FXML
    private TableColumn<?, ?> celular;

    @FXML
    private TableColumn<?, ?> correo;

    @FXML
    private TableColumn<?, ?> fechaP;

    @FXML
    private TableColumn<?, ?> colegiatura;

    @FXML
    private TableColumn<?, ?> nivel;

    @FXML
    private TableColumn<?, ?> grupo;

    @FXML
    private Button btnSalir;

    @FXML
    private TextField filterField;

    @FXML
    private ChoiceBox<String> cbFilter;

    @FXML
    private TableColumn<?, ?> factura;

    @FXML
    private TableColumn<?, ?> NResponsable;

    @FXML
    private TableColumn<?, ?> PResponsable;

    @FXML
    private TableColumn<?, ?> MResponsable;

    @FXML
    private TableColumn<?, ?> rfc;


    private String opciones[] = { "Todos", "Id", "Nombre", "Nombre Completo", "Apellido Paterno", "Apellido Materno",
            "Correo", "Telefono", "Cel", "Colegiatura", "Nivel", "Grupo", "Fecha de Nacimiento", "Fecha de Pago" };

    @FXML
    void salir(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/homeAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) apConsultarAlumno.getParent();
        pane.setCenter(root);
    }


    @FXML
    void generarPdf(ActionEvent event) throws FileNotFoundException, DocumentException {
        Document documento = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/reporteAlumnos.pdf"));
        documento.open();
        PdfPTable tabla = new PdfPTable(7);
        tabla.addCell("ID ALUMNO");
        tabla.addCell("NOMBRE");
        tabla.addCell("APELLIDO PATERNO");
        tabla.addCell("APELLIDO MATERNO");
        tabla.addCell("CELULAR");
        tabla.addCell("COLEGIATURA");
        tabla.addCell("GRUPO");

        for (int i = 0; i < tbAlumno.getItems().size(); i++){
             
            tabla.addCell(String.valueOf(tbAlumno.getItems().get(i).getIdAlumno()));  
            tabla.addCell(tbAlumno.getItems().get(i).getNombre());
            tabla.addCell(tbAlumno.getItems().get(i).getPaterno());
            tabla.addCell(tbAlumno.getItems().get(i).getMaterno());             
            tabla.addCell(tbAlumno.getItems().get(i).getCelular());
            tabla.addCell(String.valueOf(tbAlumno.getItems().get(i).getColegiatura()));             
            tabla.addCell(tbAlumno.getItems().get(i).getGrupo());             
        }
        documento.add(tabla);
        documento.close();  
    }

    @FXML
    void generarExcel(ActionEvent event) throws IOException {
        String ruta = System.getProperty("user.home");
        String excelFilePath = ruta + "/Desktop/reporteAlumnos.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        CellStyle cs = workbook.createCellStyle();
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.LEFT);
        EncabezadosExcel header = new EncabezadosExcel();
        
        header.createHeaderRow(sheet, "ALUMNOS");
        int rowCount = 0;

        for (int i = 0; i < tbAlumno.getItems().size(); i++) {
            Row row = sheet.createRow(++rowCount);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(tbAlumno.getItems().get(i).getIdAlumno());
            cell1.setCellStyle(cs);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(tbAlumno.getItems().get(i).getNombre());
            cell2.setCellStyle(cs);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(tbAlumno.getItems().get(i).getPaterno());
            cell3.setCellStyle(cs);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(tbAlumno.getItems().get(i).getMaterno());
            cell4.setCellStyle(cs);
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(tbAlumno.getItems().get(i).getCorreo());
            cell5.setCellStyle(cs);
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(tbAlumno.getItems().get(i).getRfc());
            cell6.setCellStyle(cs);
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(tbAlumno.getItems().get(i).getGrupo());
            cell7.setCellStyle(cs);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
            workbook.close();
        }

    
    }

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
    Alumno alumno = null;

    ObservableList <Alumno> ListaAlumnos = FXCollections.observableArrayList();

    private void cargarDatos() throws SQLException {
        connection = Conector.conectar();
        refrescar();
        idAlumno.setCellValueFactory(new PropertyValueFactory<>("idAlumno")); 
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre")); 
        apellidoP.setCellValueFactory(new PropertyValueFactory<>("Paterno"));
        apellidoM.setCellValueFactory(new PropertyValueFactory<>("Materno"));
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        celular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        fechaN.setCellValueFactory(new PropertyValueFactory<>("fechaNac"));
        fechaP.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        colegiatura.setCellValueFactory(new PropertyValueFactory<>("colegiatura"));
        nivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        grupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));
        factura.setCellValueFactory(new PropertyValueFactory<>("factura"));
        NResponsable.setCellValueFactory(new PropertyValueFactory<>("nombreResponsable"));
        PResponsable.setCellValueFactory(new PropertyValueFactory<>("aPaternoResponsable"));
        MResponsable.setCellValueFactory(new PropertyValueFactory<>("aMaternoResponsable"));
        rfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        
    }

    private void filtrar(){
        FilteredList<Alumno> filteredData = new FilteredList<>(ListaAlumnos, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(alumno -> {
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();

                if (cbFilter.getValue().equals("Todos")) {
                    if (String.valueOf(alumno.getIdAlumno()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getNombre().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getPaterno().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getMaterno().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getTelefono().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getCelular().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getCorreo().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (String.valueOf(alumno.getColegiatura()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (String.valueOf(alumno.getNivel()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getGrupo().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getFechaNac().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (alumno.getFechaPago().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else
                        return false;
                } else if (cbFilter.getValue().equals("Nombre Completo")) {
                    String nombre_completo = alumno.getNombre().toLowerCase() + " " + alumno.getPaterno().toLowerCase();
                    if (nombre_completo.indexOf(lowerCaseFilter) != -1)
                        return true;
                    else
                        return false;
                } else {

                    if (cbFilter.getValue().equals("Id")
                            && String.valueOf(alumno.getIdAlumno()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Nombre")
                            && alumno.getNombre().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Apellido Paterno")
                            && alumno.getPaterno().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Apellido Materno")
                            && alumno.getMaterno().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Telefono")
                            && alumno.getTelefono().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Cel")
                            && alumno.getCelular().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Correo")
                            && alumno.getCorreo().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Colegiatura")
                            && String.valueOf(alumno.getColegiatura()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Nivel")
                            && String.valueOf(alumno.getNivel()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Grupo")
                            && alumno.getGrupo().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Fecha de Nacimiento")
                            && alumno.getFechaNac().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else if (cbFilter.getValue().equals("Fecha de Pago")
                            && alumno.getFechaPago().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
                        return true;
                    else
                        return false;
                }
            });


        });

        SortedList<Alumno> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbAlumno.comparatorProperty());
        tbAlumno.setItems(sortedData); 
        
    }


    

    private void refrescar() throws SQLException {
        ListaAlumnos.clear();
        query = "SELECT a.*, g.idgrupo, g.codigo_grupo FROM alumno AS a INNER JOIN grupo_alumnos AS ga ON ga.idalumno = a.idalumno INNER JOIN grupo AS g ON g.idgrupo = ga.idgrupo";
        prep = connection.prepareStatement(query);
        res = prep.executeQuery();

        while(res.next()){

            ListaAlumnos.add( new Alumno(res.getInt("idalumno"), res.getString("nombre") , res.getString("apaterno"), res.getString("amaterno"), res.getString("telefono"), res.getString("celular"), res.getString("correo"), res.getDate("fecha_nac"), res.getDate("fecha_pago"), res.getInt("colegiatura"), res.getInt("idnivel"), res.getInt("idgrupo"), res.getString("codigo_grupo"), res.getString("nombre_res"), res.getString("apaterno_res"), res.getString("amaterno_res"),res.getString("rfc"), res.getString("factura")));
        }

        tbAlumno.setItems(ListaAlumnos);
        
        
        filtrar();
    }
    

}
