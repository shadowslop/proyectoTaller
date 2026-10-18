package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.ClienteDto;
import com.tallerfrontend.frontend.model.PrestamoResponse;
import com.tallerfrontend.frontend.service.ApiException;
import com.tallerfrontend.frontend.service.ClienteService;
import com.tallerfrontend.frontend.service.PrestamoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ClienteController {

    @FXML private TextField campoCedula;
    @FXML private TextField campoNombre;
    @FXML private TextField campoApellido;
    @FXML private TextField campoTelefono;
    @FXML private TextField campoNumeroLicencia;
    @FXML private DatePicker fechaLicencia;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelarEdicion;
    @FXML private Label labelError;

    @FXML private TextField campoBuscar;

    @FXML private TableView<ClienteDto> tablaClientes;
    @FXML private TableColumn<ClienteDto, String> colCedula;
    @FXML private TableColumn<ClienteDto, String> colNombre;
    @FXML private TableColumn<ClienteDto, String> colApellido;
    @FXML private TableColumn<ClienteDto, String> colTelefono;
    @FXML private TableColumn<ClienteDto, String> colNumeroLicencia;
    @FXML private TableColumn<ClienteDto, String> colLicencia;
    @FXML private TableColumn<ClienteDto, Void> colAccion;

    private final ClienteService clienteService = new ClienteService();
    private final PrestamoService prestamoService = new PrestamoService();
    private ClienteDto clienteEnEdicion = null;

    private final ObservableList<ClienteDto> todosClientes = FXCollections.observableArrayList();
    private FilteredList<ClienteDto> clientesFiltrados;

    @FXML
    public void initialize() {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colNumeroLicencia.setCellValueFactory(new PropertyValueFactory<>("numeroLicencia"));
        colLicencia.setCellValueFactory(cellData -> {
            var fecha = cellData.getValue().getFechaVencimientoLicencia();
            return new SimpleStringProperty(fecha != null ? fecha.toString() : "");
        });

        // RF-50: busqueda por varios criterios sobre la lista ya cargada
        clientesFiltrados = new FilteredList<>(todosClientes, c -> true);
        tablaClientes.setItems(clientesFiltrados);
        campoBuscar.textProperty().addListener((obs, anterior, texto) ->
                clientesFiltrados.setPredicate(c -> coincide(c, texto)));

        agregarBotonesAccion();
        cargarClientes();
    }

    private boolean coincide(ClienteDto c, String texto) {
        if (texto == null || texto.isBlank()) return true;
        String filtro = texto.trim().toLowerCase();
        return contiene(c.getCedula(), filtro)
                || contiene(c.getNombre(), filtro)
                || contiene(c.getApellido(), filtro)
                || contiene(c.getTelefono(), filtro)
                || contiene(c.getNumeroLicencia(), filtro);
    }

    private boolean contiene(String valor, String filtro) {
        return valor != null && valor.toLowerCase().contains(filtro);
    }

    private void cargarClientes() {
        try {
            todosClientes.setAll(clienteService.listar());
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los clientes");
        }
    }

    @FXML
    private void handleCrear() {
        String cedula = campoCedula.getText();
        String nombre = campoNombre.getText();
        String apellido = campoApellido.getText();
        String telefono = campoTelefono.getText();
        String numeroLicencia = campoNumeroLicencia.getText();
        var fecha = fechaLicencia.getValue();

        if (cedula.isBlank() || nombre.isBlank() || apellido.isBlank()
                || numeroLicencia.isBlank() || fecha == null) {
            mostrarError("Completa cedula, nombre, apellido, numero de licencia y fecha de vencimiento");
            return;
        }

        if (fecha.isBefore(java.time.LocalDate.now())) {
            mostrarError("La licencia de conduccion esta vencida");
            return;
        }

        ClienteDto datos = new ClienteDto();
        datos.setCedula(cedula);
        datos.setNombre(nombre);
        datos.setApellido(apellido);
        datos.setTelefono(telefono);
        datos.setNumeroLicencia(numeroLicencia);
        datos.setFechaVencimientoLicencia(fecha);

        try {
            if (clienteEnEdicion != null) {
                clienteService.editar(clienteEnEdicion.getCedula(), datos);
                mostrarExito("Cliente actualizado correctamente (licencia renovada)");
            } else {
                clienteService.crear(datos);
            }
            limpiarFormulario();
            cargarClientes();
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError(clienteEnEdicion != null
                    ? "No se pudo editar el cliente"
                    : "No se pudo crear el cliente (revisa si la cedula ya existe)");
        }
    }

    @FXML
    private void handleCancelarEdicion() {
        limpiarFormulario();
    }

    private void cargarEnFormulario(ClienteDto cliente) {
        clienteEnEdicion = cliente;
        campoCedula.setText(cliente.getCedula());
        campoNombre.setText(cliente.getNombre());
        campoApellido.setText(cliente.getApellido());
        campoTelefono.setText(cliente.getTelefono());
        campoNumeroLicencia.setText(cliente.getNumeroLicencia());
        fechaLicencia.setValue(cliente.getFechaVencimientoLicencia());
        campoCedula.setDisable(true); // la cedula no se puede cambiar (es la PK)
        botonGuardar.setText("Guardar cambios (renovar licencia)");
        botonCancelarEdicion.setVisible(true);
        botonCancelarEdicion.setManaged(true);
        labelError.setVisible(false);
    }

    private void limpiarFormulario() {
        clienteEnEdicion = null;
        campoCedula.setDisable(false);
        campoCedula.clear();
        campoNombre.clear();
        campoApellido.clear();
        campoTelefono.clear();
        campoNumeroLicencia.clear();
        fechaLicencia.setValue(null);
        botonGuardar.setText("Crear cliente");
        botonCancelarEdicion.setVisible(false);
        botonCancelarEdicion.setManaged(false);
        tablaClientes.getSelectionModel().clearSelection();
        labelError.setVisible(false);
    }

    private void agregarBotonesAccion() {
        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button botonEditar = new Button("Editar");
            private final Button botonEliminar = new Button("Eliminar");
            private final Button botonHistorial = new Button("Historial");
            private final HBox contenedor = new HBox(6, botonEditar, botonEliminar, botonHistorial);

            {
                botonEditar.setStyle("-fx-padding: 6 10 6 10; -fx-font-size: 12px;");
                botonEliminar.setStyle("-fx-padding: 6 10 6 10; -fx-font-size: 12px;");
                botonHistorial.setStyle("-fx-padding: 6 10 6 10; -fx-font-size: 12px;");

                botonEditar.setOnAction(e -> {
                    ClienteDto cliente = getTableView().getItems().get(getIndex());
                    cargarEnFormulario(cliente);
                });
                botonEliminar.setOnAction(e -> {
                    ClienteDto cliente = getTableView().getItems().get(getIndex());
                    eliminarCliente(cliente);
                });
                botonHistorial.setOnAction(e -> {
                    ClienteDto cliente = getTableView().getItems().get(getIndex());
                    verHistorial(cliente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    // RF-28: consultar el historial de alquileres realizados por un cliente
    private void verHistorial(ClienteDto cliente) {
        try {
            var prestamos = prestamoService.listarPorCliente(cliente.getCedula());

            TableView<PrestamoResponse> tabla = new TableView<>();

            TableColumn<PrestamoResponse, String> colVehiculo = new TableColumn<>("Vehiculo");
            colVehiculo.setCellValueFactory(new PropertyValueFactory<>("vehiculoPlacaTexto"));

            TableColumn<PrestamoResponse, String> colEstado = new TableColumn<>("Estado");
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoTexto"));

            TableColumn<PrestamoResponse, java.math.BigDecimal> colMonto = new TableColumn<>("Monto");
            colMonto.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

            tabla.getColumns().addAll(colVehiculo, colEstado, colMonto);
            tabla.setItems(FXCollections.observableArrayList(prestamos));
            tabla.setPrefSize(500, 300);

            Alert ventana = new Alert(Alert.AlertType.INFORMATION);
            ventana.setTitle("Historial de alquileres");
            ventana.setHeaderText(cliente.getNombre() + " " + cliente.getApellido()
                    + " (cedula " + cliente.getCedula() + ")");

            if (prestamos.isEmpty()) {
                ventana.setContentText("Este cliente aun no tiene alquileres registrados.");
            } else {
                ventana.getDialogPane().setContent(tabla);
            }
            ventana.showAndWait();
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("No se pudo cargar el historial del cliente");
        }
    }

    private void eliminarCliente(ClienteDto cliente) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el cliente " + cliente.getNombre() + " " + cliente.getApellido()
                        + " (cedula " + cliente.getCedula() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    clienteService.eliminar(cliente.getCedula());
                    if (clienteEnEdicion != null && clienteEnEdicion.getCedula().equals(cliente.getCedula())) {
                        limpiarFormulario();
                    }
                    cargarClientes();
                } catch (ApiException e) {
                    mostrarError(e.getMessage());
                } catch (Exception e) {
                    mostrarError("No se pudo eliminar el cliente");
                }
            }
        });
    }

    private void mostrarError(String mensaje) {
        labelError.setStyle("-fx-text-fill: red;");
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }

    private void mostrarExito(String mensaje) {
        labelError.setStyle("-fx-text-fill: green;");
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }
}