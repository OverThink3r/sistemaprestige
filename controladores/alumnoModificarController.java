package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
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
import javafx.scene.layout.BorderPane;
import modelos.Conector;
import objetos.Alumno;
import javafx.scene.Parent;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class alumnoModificarController implements Initializable {
    private BorderPane pane;

    @FXML
    private AnchorPane myAnchor;

    @FXML
    private TableColumn<Alumno, Alumno> accion;

    @FXML
    private TableColumn<?, ?> idAlumno;

    @FXML
    private Button salir;

    @FXML
    private TableColumn<?, ?> apellidoP;

    @FXML
    private TableColumn<?, ?> grupo;

    @FXML
    private TableView<Alumno> tbAlumno;

    @FXML
    private TableColumn<?, ?> nombre;

    @FXML
    private TextField filterField;

    @FXML
    private TableColumn<?, ?> colegiatura;

    @FXML
    private TableColumn<?, ?> correo;

    @FXML
    private TableColumn<?, ?> celular;

    @FXML
    private TableColumn<?, ?> apellidoM;

    @FXML
    private TableColumn<?, ?> telefono;

    @FXML
    private TableColumn<?, ?> fechaN;

    @FXML
    private TableColumn<?, ?> nivel;

    @FXML
    private TableColumn<?, ?> fechaP;

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
    void salirModificar(ActionEvent event) throws IOException {
  

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/homeAlumno.fxml"));
        Parent root = loader.load();
        pane = (BorderPane) myAnchor.getParent();
        pane.setCenter(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accion.setStyle("-fx-alignment: CENTER;");
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

    ObservableList<Alumno> ListaAlumnos = FXCollections.observableArrayList();

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
        accion.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        accion.setCellFactory(param -> new TableCell<Alumno, Alumno>() {
            private final Button deleteButton = new Button("Modificar");

            @Override
            protected void updateItem(Alumno alumno, boolean empty) {
                super.updateItem(alumno, empty);

                if (alumno == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction((event) -> {
                    try {

                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/vistas/modificarAlumnoEmergente.fxml"));
                        Parent root;
                        root = loader.load();
                        pane = (BorderPane) myAnchor.getParent();
                        alumnoModificarEmergenteController obj = loader.getController();

                        obj.setDatos(alumno.getIdAlumno(), alumno.getNombre(), alumno.getPaterno(), alumno.getMaterno(),
                                alumno.getTelefono(), alumno.getCelular(), alumno.getCorreo(), alumno.getFechaNac(),
                                alumno.getFechaPago(), alumno.getColegiatura(), alumno.getNivel(), alumno.getIdGrupo(),
                                alumno.getGrupo(), alumno.getNombreResponsable(), alumno.getAPaternoResponsable(),
                                alumno.getAMaternoResponsable(), alumno.getRfc(), alumno.getFactura());

                        pane.setCenter(root);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }
        });

    }

    private void filtrar() {
        FilteredList<Alumno> filteredData = new FilteredList<>(ListaAlumnos, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(alumno -> {
                if (newValue == null || newValue.isEmpty())
                    return true;

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

        while (res.next()) {
            ListaAlumnos.add(new Alumno(res.getInt("idalumno"), res.getString("nombre"), res.getString("apaterno"),
                    res.getString("amaterno"), res.getString("telefono"), res.getString("celular"),
                    res.getString("correo"), res.getDate("fecha_nac"), res.getDate("fecha_pago"),
                    res.getInt("colegiatura"), res.getInt("idnivel"), res.getInt("idgrupo"),
                    res.getString("codigo_grupo"), res.getString("nombre_res"), res.getString("apaterno_res"),
                    res.getString("amaterno_res"), res.getString("rfc"), res.getString("factura")));
        }

        tbAlumno.setItems(ListaAlumnos);
        filtrar();
    }
}
