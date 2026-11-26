package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.dao.MascotaDAO;
import cl.duoc.pataverde.dao.PersistenciaException;
import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Locale;

/**
 * Controlador de la ventana principal: formulario, tabla y área de mensajes.
 * Accede a los datos únicamente a través de la interfaz MascotaDAO.
 */
public class MainController {

    // =====================================================================
    // EP3: asigna aquí tu implementación de MascotaDAO
    //      (por ejemplo: = new MiImplementacionDAO();).
    //      No es necesario modificar ninguna otra parte de esta clase.
    // =====================================================================
    private final MascotaDAO dao = null;

    private static final Locale LOCALE_CL = Locale.forLanguageTag("es-CL");

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtPeso;
    @FXML private TextField txtRaza;
    @FXML private CheckBox chkVacunado;
    @FXML private CheckBox chkAdopcion;
    @FXML private CheckBox chkExterior;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;

    @FXML private TableView<Mascota> tabla;
    @FXML private TableColumn<Mascota, Integer> colId;
    @FXML private TableColumn<Mascota, String> colTipo;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, Integer> colEdad;
    @FXML private TableColumn<Mascota, Double> colPeso;
    @FXML private TableColumn<Mascota, String> colCosto;

    @FXML private Label lblMensaje;

    /** Mascota que se está editando; null cuando el formulario registra una nueva. */
    private Mascota mascotaEnEdicion;

    @FXML
    private void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList(Perro.TIPO, Gato.TIPO));
        cbTipo.valueProperty().addListener((obs, anterior, actual) -> actualizarCamposPorTipo());

        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colEdad.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getEdad()));
        colPeso.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPeso()));
        colCosto.setCellValueFactory(c ->
                new SimpleStringProperty(formatearCosto(c.getValue().calcularCostoConsulta())));
        tabla.setPlaceholder(new Label("No hay mascotas para mostrar."));

        limpiarFormulario();
        cargarTabla();
    }

    // ---------------------------------------------------------------- acciones

    @FXML
    private void onRegistrar() {
        if (!hayDao()) {
            return;
        }
        Mascota mascota;
        try {
            mascota = crearMascotaDesdeFormulario();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
            return;
        }

        try {
            if (mascotaEnEdicion == null) {
                dao.insertar(mascota);
                cargarTabla();
                limpiarFormulario();
                mostrarExito(describir(mascota) + " registrado correctamente.");
            } else {
                mascota.setId(mascotaEnEdicion.getId());
                dao.actualizar(mascota);
                cargarTabla();
                limpiarFormulario();
                mostrarExito(describir(mascota) + " actualizado correctamente.");
            }
        } catch (PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditar() {
        Mascota seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una mascota de la tabla para editar.");
            return;
        }
        mascotaEnEdicion = seleccionada;
        cbTipo.setValue(seleccionada.getTipo());
        cbTipo.setDisable(true);
        txtNombre.setText(seleccionada.getNombre());
        txtEdad.setText(String.valueOf(seleccionada.getEdad()));
        txtPeso.setText(String.valueOf(seleccionada.getPeso()));
        if (seleccionada instanceof Perro perro) {
            txtRaza.setText(perro.getRaza());
            chkVacunado.setSelected(perro.isVacunado());
            chkAdopcion.setSelected(perro.isDisponibleAdopcion());
        } else if (seleccionada instanceof Gato gato) {
            chkExterior.setSelected(gato.isViveExterior());
        }
        btnRegistrar.setText("Guardar cambios");
        btnCancelar.setDisable(false);
        txtNombre.requestFocus();
        mostrarInfo("Editando a " + describir(seleccionada)
                + ". Modifica los datos y presiona Guardar cambios.");
    }

    @FXML
    private void onCancelar() {
        limpiarFormulario();
        mostrarInfo("Edición cancelada.");
    }

    @FXML
    private void onEliminar() {
        if (!hayDao()) {
            return;
        }
        Mascota seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una mascota de la tabla para eliminar.");
            return;
        }
        try {
            dao.eliminar(seleccionada.getId());
            if (mascotaEnEdicion != null && mascotaEnEdicion.getId() == seleccionada.getId()) {
                limpiarFormulario();
            }
            cargarTabla();
            mostrarExito(describir(seleccionada) + " eliminado correctamente.");
        } catch (PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    // ---------------------------------------------------------------- apoyo

    /** Vuelve a leer todas las mascotas desde el almacenamiento y las muestra en la tabla. */
    private void cargarTabla() {
        if (!hayDao()) {
            tabla.getItems().clear();
            return;
        }
        try {
            tabla.setItems(FXCollections.observableArrayList(dao.obtenerTodos()));
        } catch (PersistenciaException e) {
            tabla.getItems().clear();
            mostrarError("Error al leer la información: " + e.getMessage());
        }
    }

    private boolean hayDao() {
        if (dao == null) {
            mostrarError("Aún no hay una implementación de MascotaDAO asignada en MainController.");
            return false;
        }
        return true;
    }

    private Mascota crearMascotaDesdeFormulario() {
        String tipo = cbTipo.getValue();
        if (tipo == null) {
            throw new IllegalArgumentException("Selecciona el tipo de mascota.");
        }
        String nombre = txtNombre.getText();
        int edad = leerEntero(txtEdad.getText(), "La edad debe ser un número entero.");
        double peso = leerDecimal(txtPeso.getText(), "El peso debe ser un número.");

        if (Perro.TIPO.equals(tipo)) {
            return new Perro(nombre, edad, peso, txtRaza.getText(),
                    chkVacunado.isSelected(), chkAdopcion.isSelected());
        }
        return new Gato(nombre, edad, peso, chkExterior.isSelected());
    }

    private int leerEntero(String texto, String mensajeError) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private double leerDecimal(String texto, String mensajeError) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private void actualizarCamposPorTipo() {
        boolean esPerro = Perro.TIPO.equals(cbTipo.getValue());
        boolean esGato = Gato.TIPO.equals(cbTipo.getValue());
        txtRaza.setDisable(!esPerro);
        chkVacunado.setDisable(!esPerro);
        chkAdopcion.setDisable(!esPerro);
        chkExterior.setDisable(!esGato);
    }

    private void limpiarFormulario() {
        mascotaEnEdicion = null;
        cbTipo.setDisable(false);
        cbTipo.setValue(null);
        txtNombre.clear();
        txtEdad.clear();
        txtPeso.clear();
        txtRaza.clear();
        chkVacunado.setSelected(false);
        chkAdopcion.setSelected(false);
        chkExterior.setSelected(false);
        btnRegistrar.setText("Registrar");
        btnCancelar.setDisable(true);
        actualizarCamposPorTipo();
        tabla.getSelectionModel().clearSelection();
    }

    private String describir(Mascota mascota) {
        return mascota.getNombre() + " (" + mascota.getTipo() + ")";
    }

    private String formatearCosto(double costo) {
        return String.format(LOCALE_CL, "$%,.0f", costo);
    }

    private void mostrarExito(String mensaje) {
        mostrarMensaje(mensaje, "#eef5ec", "#a9cfa1", "#1e4620");
    }

    private void mostrarInfo(String mensaje) {
        mostrarMensaje(mensaje, "#eef3f8", "#a8c1d9", "#1f3a56");
    }

    private void mostrarError(String mensaje) {
        mostrarMensaje(mensaje, "#fbeeee", "#e0a3a3", "#8a1f1f");
    }

    private void mostrarMensaje(String mensaje, String fondo, String borde, String texto) {
        lblMensaje.setStyle("-fx-padding: 8 10 8 10; -fx-background-color: " + fondo
                + "; -fx-border-color: " + borde + "; -fx-text-fill: " + texto + ";");
        lblMensaje.setText(mensaje);
    }
}
