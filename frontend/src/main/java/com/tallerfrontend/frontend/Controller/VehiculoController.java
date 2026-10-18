package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.SedeDto;
import com.tallerfrontend.frontend.model.TipoVehiculoDto;
import com.tallerfrontend.frontend.model.VehiculoCreateRequest;
import com.tallerfrontend.frontend.model.VehiculoDto;
import com.tallerfrontend.frontend.service.ApiException;
import com.tallerfrontend.frontend.service.SedeService;
import com.tallerfrontend.frontend.service.TipoVehiculoService;
import com.tallerfrontend.frontend.service.VehiculoService;
import com.tallerfrontend.frontend.util.SesionActual;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.Objects;

public class VehiculoController {

    @FXML private HBox panelCrear;
    @FXML private TextField campoPlaca;
    @FXML private ComboBox<TipoVehiculoDto> comboTipo;
    @FXML private TextField campoPrecioDia;
    @FXML private ComboBox<SedeDto> comboSede;
    @FXML private Label labelError;

    // ---------- Filtros (HU-05 / RF-50) ----------
    @FXML private TextField campoFiltro;
    @FXML private ComboBox<SedeDto> filtroSede;
    @FXML private ComboBox<TipoVehiculoDto> filtroTipo;
    @FXML private ComboBox<String> filtroEstado;

    @FXML private TableView<VehiculoDto> tablaVehiculos;
    @FXML private TableColumn<VehiculoDto, String> colPlaca;
    @FXML private TableColumn<VehiculoDto, String> colTipo;
    @FXML private TableColumn<VehiculoDto, BigDecimal> colPrecioDia;
    @FXML private TableColumn<VehiculoDto, String> colEstado;
    @FXML private TableColumn<VehiculoDto, String> colSede;
    @FXML private TableColumn<VehiculoDto, Void> colAccion;

    @FXML private VBox panelSuperAdmin;
    @FXML private ComboBox<SedeDto> comboSedeDestino;
    @FXML private Label labelVehiculoSeleccionado;
    @FXML private Label labelErrorSede;
    @FXML private ListView<String> listaConteoPorSede;

    private final VehiculoService vehiculoService = new VehiculoService();
    private final SedeService sedeService = new SedeService();
    private final TipoVehiculoService tipoVehiculoService = new TipoVehiculoService();

    private final ObservableList<VehiculoDto> todosVehiculos = FXCollections.observableArrayList();
    private FilteredList<VehiculoDto> vehiculosFiltrados;

    @FXML
    public void initialize() {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoNombre"));
        colPrecioDia.setCellValueFactory(new PropertyValueFactory<>("precioDia"));
        colEstado.setCellValueFactory(cellData -> {
            Boolean estado = cellData.getValue().getEstado();
            String texto = Boolean.TRUE.equals(estado) ? "Disponible" : "Prestado";
            return new SimpleStringProperty(texto);
        });
        colSede.setCellValueFactory(cellData -> {
            SedeDto sede = cellData.getValue().getSedeId();
            return new SimpleStringProperty(sede != null ? sede.getNombre() : "");
        });

        // Lista filtrable: se carga UNA vez en todosVehiculos y la tabla muestra la vista filtrada
        vehiculosFiltrados = new FilteredList<>(todosVehiculos, v -> true);
        tablaVehiculos.setItems(vehiculosFiltrados);

        filtroEstado.setItems(FXCollections.observableArrayList("Todos", "Disponible", "Prestado"));
        filtroEstado.setValue("Todos");

        campoFiltro.textProperty().addListener((obs, a, n) -> aplicarFiltros());
        filtroSede.valueProperty().addListener((obs, a, n) -> aplicarFiltros());
        filtroTipo.valueProperty().addListener((obs, a, n) -> aplicarFiltros());
        filtroEstado.valueProperty().addListener((obs, a, n) -> aplicarFiltros());

        cargarTiposVehiculo();

        // Autocompletar el precio por dia con el precio base del tipo seleccionado.
        // El campo sigue siendo editable por si quieren un precio distinto para ese vehiculo puntual.
        comboTipo.valueProperty().addListener((obs, tipoAnterior, tipoNuevo) -> {
            if (tipoNuevo != null && tipoNuevo.getPrecioDia() != null) {
                campoPrecioDia.setText(tipoNuevo.getPrecioDia().toBigInteger().toString());
            }
        });

        boolean esSuperAdmin = "SUPERADMIN".equals(SesionActual.getInstancia().getRol());

        // Solo el SUPERADMIN registra vehiculos (el backend lo exige); los demas roles solo consultan
        panelCrear.setVisible(esSuperAdmin);
        panelCrear.setManaged(esSuperAdmin);

        panelSuperAdmin.setVisible(esSuperAdmin);
        panelSuperAdmin.setManaged(esSuperAdmin);
        agregarBotonEliminar(esSuperAdmin);

        cargarSedes();
        cargarVehiculos();
        if (esSuperAdmin) {
            cargarConteoPorSede();
        }

        tablaVehiculos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                labelVehiculoSeleccionado.setText("Seleccionado: " + seleccionado.getPlaca()
                        + " (sede actual: " + (seleccionado.getSedeId() != null ? seleccionado.getSedeId().getNombre() : "-") + ")");
            } else {
                labelVehiculoSeleccionado.setText("Selecciona un vehiculo en la tabla");
            }
        });
    }

    // ---------- Filtros ----------

    private void aplicarFiltros() {
        vehiculosFiltrados.setPredicate(this::coincide);
    }

    private boolean coincide(VehiculoDto v) {
        String texto = campoFiltro.getText() == null ? "" : campoFiltro.getText().trim().toLowerCase();
        if (!texto.isBlank()) {
            String placa = v.getPlaca() == null ? "" : v.getPlaca().toLowerCase();
            String tipo = v.getTipoNombre() == null ? "" : v.getTipoNombre().toLowerCase();
            if (!placa.contains(texto) && !tipo.contains(texto)) return false;
        }

        SedeDto sede = filtroSede.getValue();
        if (sede != null) {
            if (v.getSedeId() == null || !Objects.equals(v.getSedeId().getId(), sede.getId())) return false;
        }

        TipoVehiculoDto tipo = filtroTipo.getValue();
        if (tipo != null && !tipo.getNombre().equalsIgnoreCase(v.getTipoNombre())) return false;

        String estado = filtroEstado.getValue();
        boolean disponible = Boolean.TRUE.equals(v.getEstado());
        if ("Disponible".equals(estado) && !disponible) return false;
        if ("Prestado".equals(estado) && disponible) return false;

        return true;
    }

    @FXML
    private void handleLimpiarFiltros() {
        campoFiltro.clear();
        filtroSede.setValue(null);
        filtroTipo.setValue(null);
        filtroEstado.setValue("Todos");
    }

    // ---------- Carga de datos ----------

    private void cargarTiposVehiculo() {
        try {
            var tipos = tipoVehiculoService.listarActivos();
            comboTipo.setItems(FXCollections.observableArrayList(tipos));
            filtroTipo.setItems(FXCollections.observableArrayList(tipos));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los tipos de vehiculo");
        }
    }

    private void cargarSedes() {
        try {
            var sedes = sedeService.listar();
            comboSede.setItems(FXCollections.observableArrayList(sedes));
            comboSedeDestino.setItems(FXCollections.observableArrayList(sedes));
            filtroSede.setItems(FXCollections.observableArrayList(sedes));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar las sedes");
        }
    }

    private void cargarVehiculos() {
        try {
            todosVehiculos.setAll(vehiculoService.listar());
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los vehiculos");
        }
    }

    private void cargarConteoPorSede() {
        try {
            var conteos = vehiculoService.contarPorSede();
            listaConteoPorSede.setItems(FXCollections.observableArrayList(
                    conteos.stream().map(Object::toString).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErrorSede("No se pudo cargar el conteo: " + e.getMessage());
        }
    }

    @FXML
    private void handleCrear() {
        String placa = campoPlaca.getText();
        TipoVehiculoDto tipo = comboTipo.getValue();
        String precioTexto = campoPrecioDia.getText();
        SedeDto sede = comboSede.getValue();

        if (placa.isBlank() || tipo == null || precioTexto.isBlank() || sede == null) {
            mostrarError("Completa todos los campos");
            return;
        }

        BigDecimal precioDia;
        try {
            precioDia = parsearPrecio(precioTexto);
        } catch (NumberFormatException e) {
            mostrarError("El precio por dia debe ser un numero valido");
            return;
        }

        VehiculoCreateRequest nuevo = new VehiculoCreateRequest(placa, tipo.getId(), precioDia, sede.getId());

        try {
            vehiculoService.crear(nuevo);
            limpiarFormulario();
            cargarVehiculos();
            if (panelSuperAdmin.isVisible()) cargarConteoPorSede();
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("No se pudo crear el vehiculo");
        }
    }

    // "120.000" o "120,000" -> 120000. Se quita todo lo que no sea digito y se
    // interpreta como pesos enteros (sin decimales), que es como se maneja el precio/dia aqui.
    private BigDecimal parsearPrecio(String texto) {
        String soloDigitos = texto.replaceAll("[^0-9]", "");
        if (soloDigitos.isBlank()) {
            throw new NumberFormatException("Precio vacio");
        }
        return new BigDecimal(soloDigitos);
    }

    @FXML
    private void handleCambiarSede() {
        VehiculoDto seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        SedeDto sedeDestino = comboSedeDestino.getValue();

        if (seleccionado == null) {
            mostrarErrorSede("Selecciona un vehiculo en la tabla");
            return;
        }
        if (sedeDestino == null) {
            mostrarErrorSede("Selecciona la sede de destino");
            return;
        }

        try {
            vehiculoService.cambiarSede(seleccionado.getPlaca(), sedeDestino.getId());
            labelErrorSede.setVisible(false);
            comboSedeDestino.setValue(null);
            cargarVehiculos();
            cargarConteoPorSede();
        } catch (ApiException e) {
            mostrarErrorSede(e.getMessage());
        } catch (Exception e) {
            mostrarErrorSede("No se pudo cambiar la sede del vehiculo");
        }
    }

    private void agregarBotonEliminar(boolean esSuperAdmin) {
        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button boton = new Button("Eliminar");
            {
                boton.setOnAction(e -> {
                    VehiculoDto vehiculo = getTableView().getItems().get(getIndex());
                    eliminarVehiculo(vehiculo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || !esSuperAdmin ? null : boton);
            }
        });
    }

    private void eliminarVehiculo(VehiculoDto vehiculo) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el vehiculo " + vehiculo.getPlaca() + "? Esto tambien borra sus documentos asociados.",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    vehiculoService.eliminar(vehiculo.getPlaca());
                    cargarVehiculos();
                    cargarConteoPorSede();
                } catch (ApiException e) {
                    mostrarError(e.getMessage());
                } catch (Exception e) {
                    mostrarError("No se pudo eliminar el vehiculo");
                }
            }
        });
    }

    private void limpiarFormulario() {
        campoPlaca.clear();
        comboTipo.setValue(null);
        campoPrecioDia.clear();
        comboSede.setValue(null);
        labelError.setVisible(false);
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }

    private void mostrarErrorSede(String mensaje) {
        labelErrorSede.setText(mensaje);
        labelErrorSede.setVisible(true);
    }
}