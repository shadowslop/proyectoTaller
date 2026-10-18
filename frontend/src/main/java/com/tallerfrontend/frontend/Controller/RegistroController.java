package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.*;
import com.tallerfrontend.frontend.service.*;
import com.tallerfrontend.frontend.util.SesionActual;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.UUID;

public class RegistroController {

    @FXML private ComboBox<ClienteDto> comboCliente;
    @FXML private ComboBox<VehiculoDto> comboVehiculo;
    @FXML private Label labelError;

    @FXML private TableView<PrestamoResponse> tablaPrestamos;
    @FXML private TableColumn<PrestamoResponse, String> colCliente;
    @FXML private TableColumn<PrestamoResponse, String> colVehiculo;
    @FXML private TableColumn<PrestamoResponse, String> colEstado;
    @FXML private TableColumn<PrestamoResponse, java.math.BigDecimal> colMonto;
    @FXML private TableColumn<PrestamoResponse, Void> colAccion;
    @FXML private TextField campoBuscarPlaca;
    @FXML private Label labelResultadoBusqueda;
    @FXML private Button botonDevolverAqui;
    @FXML private Label labelErrorBusqueda;
    @FXML private TextField campoFiltroPrestamos;

    private PrestamoResponse prestamoEncontrado;

    private final ClienteService clienteService = new ClienteService();
    private final VehiculoService vehiculoService = new VehiculoService();
    private final PrestamoService prestamoService = new PrestamoService();
    private final PagoService pagoService = new PagoService();
    private final ObservableList<PrestamoResponse> todosPrestamos = FXCollections.observableArrayList();
    private FilteredList<PrestamoResponse> prestamosFiltrados;

    // Listas fijas: se actualiza su CONTENIDO (setAll), nunca se reemplaza el objeto lista.
    private final ObservableList<ClienteDto> itemsCliente = FXCollections.observableArrayList();
    private final ObservableList<VehiculoDto> itemsVehiculo = FXCollections.observableArrayList();

    // Evita que un texto escrito por el usuario se pise con el texto que ponemos
    // nosotros mismos al seleccionar un item del combo.
    private boolean actualizandoTextoProgramaticamente = false;

    @FXML
    public void initialize() {
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNombre"));
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("vehiculoPlacaTexto"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoTexto"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        prestamosFiltrados = new FilteredList<>(todosPrestamos, p -> true);
        tablaPrestamos.setItems(prestamosFiltrados);
        campoFiltroPrestamos.textProperty().addListener((obs, a, texto) ->
                prestamosFiltrados.setPredicate(p -> coincidePrestamo(p, texto)));
        agregarBotonDevolver();

        configurarComboCliente();
        configurarComboVehiculo();

        cargarHistorial();
    }

    // ---------- Combo de cliente con autocompletado (prestamo) ----------

    private void configurarComboCliente() {
        comboCliente.setItems(itemsCliente);
        comboCliente.setEditable(true);
        comboCliente.setConverter(new StringConverter<>() {
            @Override
            public String toString(ClienteDto c) { return c == null ? "" : c.toString(); }
            @Override
            public ClienteDto fromString(String s) { return comboCliente.getValue(); }
        });

        buscarClientes(itemsCliente, comboCliente, "");

        comboCliente.getEditor().textProperty().addListener((obs, textoViejo, textoNuevo) -> {
            if (actualizandoTextoProgramaticamente) return;
            ClienteDto actual = comboCliente.getValue();
            if (actual != null && actual.toString().equals(textoNuevo)) return;
            buscarClientes(itemsCliente, comboCliente, textoNuevo);
        });

        comboCliente.setOnAction(e -> {
            ClienteDto seleccionado = comboCliente.getValue();
            if (seleccionado != null) {
                actualizandoTextoProgramaticamente = true;
                comboCliente.getEditor().setText(seleccionado.toString());
                actualizandoTextoProgramaticamente = false;
            }
        });
    }

    private void buscarClientes(ObservableList<ClienteDto> destino, ComboBox<ClienteDto> combo, String texto) {
        try {
            var resultados = clienteService.buscar(texto);
            Platform.runLater(() -> {
                destino.setAll(resultados);
                if (!resultados.isEmpty() && !combo.isShowing()) {
                    combo.show();
                }
            });
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los clientes");
        }
    }

    // ---------- Combo de vehiculo con autocompletado (prestamo, solo disponibles ahora mismo) ----------

    private void configurarComboVehiculo() {
        comboVehiculo.setItems(itemsVehiculo);
        comboVehiculo.setEditable(true);
        comboVehiculo.setConverter(new StringConverter<>() {
            @Override
            public String toString(VehiculoDto v) { return v == null ? "" : v.toString(); }
            @Override
            public VehiculoDto fromString(String s) { return comboVehiculo.getValue(); }
        });

        buscarVehiculos("");

        comboVehiculo.getEditor().textProperty().addListener((obs, textoViejo, textoNuevo) -> {
            if (actualizandoTextoProgramaticamente) return;
            VehiculoDto actual = comboVehiculo.getValue();
            if (actual != null && actual.toString().equals(textoNuevo)) return;
            buscarVehiculos(textoNuevo);
        });

        comboVehiculo.setOnAction(e -> {
            VehiculoDto seleccionado = comboVehiculo.getValue();
            if (seleccionado != null) {
                actualizandoTextoProgramaticamente = true;
                comboVehiculo.getEditor().setText(seleccionado.toString());
                actualizandoTextoProgramaticamente = false;
            }
        });
    }

    private void buscarVehiculos(String texto) {
        try {
            var sedeActualId = SesionActual.getInstancia().getSedeId();
            var resultados = vehiculoService.buscarDisponiblesPorSede(sedeActualId, texto);
            Platform.runLater(() -> {
                itemsVehiculo.setAll(resultados);
                if (!resultados.isEmpty() && !comboVehiculo.isShowing()) {
                    comboVehiculo.show();
                }
            });
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los vehiculos disponibles");
        }
    }

    private void cargarHistorial() {
        try {
            var sedeActualId = SesionActual.getInstancia().getSedeId();
            todosPrestamos.setAll(prestamoService.listarPorSede(sedeActualId));
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar el historial");
        }
    }

    @FXML
    private void handleCrearPrestamo() {
        ClienteDto cliente = comboCliente.getValue();
        VehiculoDto vehiculo = comboVehiculo.getValue();

        if (cliente == null || vehiculo == null) {
            mostrarError("Selecciona cliente y vehiculo de la lista desplegable");
            return;
        }

        var sesion = SesionActual.getInstancia();
        PrestamoCreateRequest datos = new PrestamoCreateRequest(
                cliente.getCedula(), vehiculo.getPlaca(), sesion.getUsuarioId(), sesion.getSedeId()
        );

        try {
            var creado = prestamoService.crear(datos);
            // RF-34 / RF-37: registrar kilometraje y combustible al ENTREGAR el vehiculo
            TextInputDialog dialogKm = new TextInputDialog();
            dialogKm.setTitle("Entrega de vehiculo");
            dialogKm.setHeaderText("Kilometraje actual del vehiculo (RF-34):");
            dialogKm.setContentText("Km inicial:");
            var km = dialogKm.showAndWait();

            TextInputDialog dialogComb = new TextInputDialog();
            dialogComb.setHeaderText("Nivel de combustible (RF-37):");
            dialogComb.setContentText("Combustible inicial (%):");
            var comb = dialogComb.showAndWait();

            if (km.isPresent() || comb.isPresent()) {
                Integer kmIni = null, combIni = null;
                try { kmIni = Integer.valueOf(km.get().trim()); } catch (Exception ignored) {}
                try { combIni = Integer.valueOf(comb.get().trim()); } catch (Exception ignored) {}
                prestamoService.registrarEntrega(creado.getId(), new EntregaRequest(kmIni, combIni));
            }

            labelError.setVisible(false);
            comboCliente.setValue(null);
            comboCliente.getEditor().clear();
            comboVehiculo.setValue(null);
            comboVehiculo.getEditor().clear();
            buscarVehiculos("");
            cargarHistorial();
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("No se pudo registrar el prestamo");
        }
    }

    private void agregarBotonDevolver() {
        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button boton = new Button("Devolver");
            {
                boton.setOnAction(e -> {
                    PrestamoResponse prestamo = getTableView().getItems().get(getIndex());
                    devolverPrestamo(prestamo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    PrestamoResponse prestamo = getTableView().getItems().get(getIndex());
                    setGraphic(prestamo.isEstado() ? boton : null);
                }
            }
        });
    }

    private void devolverPrestamo(PrestamoResponse prestamo) {
        // --- Dialogo de devolucion (RF-35/36/37/38/39/40) ---
        Dialog<DevolucionRequest> dialog = new Dialog<>();
        dialog.setTitle("Registrar devolucion");
        dialog.setHeaderText("Vehiculo: " + prestamo.getVehiculoPlacaTexto());

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        TextField campoKmFinal = new TextField(); campoKmFinal.setPromptText("Km final");
        TextField campoCombFinal = new TextField(); campoCombFinal.setPromptText("Combustible final (%)");
        TextArea campoNovedades = new TextArea(); campoNovedades.setPromptText("Danos o novedades (RF-38/39)");
        campoNovedades.setPrefRowCount(3);
        ComboBox<String> comboEstadoFinal = new ComboBox<>(FXCollections.observableArrayList(
                "DISPONIBLE", "EN_MANTENIMIENTO", "FUERA_DE_SERVICIO"));
        comboEstadoFinal.setValue("DISPONIBLE");

        grid.addRow(0, new Label("Km final:"), campoKmFinal);
        grid.addRow(1, new Label("Combustible final:"), campoCombFinal);
        grid.addRow(2, new Label("Estado final:"), comboEstadoFinal);
        grid.addRow(3, new Label("Novedades:"), campoNovedades);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(boton -> {
            if (boton != ButtonType.OK) return null;
            var sedeActualId = SesionActual.getInstancia().getSedeId();
            DevolucionRequest req = new DevolucionRequest(sedeActualId);
            try { req.setKmFinal(Integer.valueOf(campoKmFinal.getText().trim())); } catch (Exception ignored) {}
            try { req.setCombFinal(Integer.valueOf(campoCombFinal.getText().trim())); } catch (Exception ignored) {}
            req.setNovedades(campoNovedades.getText().isBlank() ? null : campoNovedades.getText());
            req.setEstadoFinalVehiculo(comboEstadoFinal.getValue());
            return req;
        });

        dialog.showAndWait().ifPresent(req -> {
            try {
                var devuelto = prestamoService.devolver(prestamo.getId(), req);
                buscarVehiculos("");
                cargarHistorial();
                // --- Pago y comprobante (RF-42/43/44) ---
                registrarPagoYComprobante(devuelto);
            } catch (Exception e) {
                mostrarError("No se pudo procesar la devolucion");
            }
        });
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }

    @FXML
    private void handleBuscarPlaca() {
        String placa = campoBuscarPlaca.getText();

        if (placa.isBlank()) {
            mostrarErrorBusqueda("Escribe una placa");
            return;
        }

        try {
            prestamoEncontrado = prestamoService.buscarActivoPorPlaca(placa);
            labelResultadoBusqueda.setText("Cliente: " + prestamoEncontrado.getClienteNombre()
                    + " | Prestado en: " + prestamoEncontrado.getSedePrestamo().getNombre());
            botonDevolverAqui.setVisible(true);
            botonDevolverAqui.setManaged(true);
            labelErrorBusqueda.setVisible(false);
        } catch (Exception e) {
            prestamoEncontrado = null;
            labelResultadoBusqueda.setText("");
            botonDevolverAqui.setVisible(false);
            botonDevolverAqui.setManaged(false);
            mostrarErrorBusqueda("No hay un prestamo activo con esa placa");
        }
    }

    @FXML
    private void registrarPagoYComprobante(PrestamoResponse prestamo) {
        ChoiceDialog<String> dialogPago = new ChoiceDialog<>("EFECTIVO",
                "EFECTIVO", "TARJETA", "TRANSFERENCIA");
        dialogPago.setTitle("Registrar pago");
        String mora = (prestamo.getRecargoMora() != null && prestamo.getRecargoMora().signum() > 0)
                ? " (incluye mora de $" + prestamo.getRecargoMora() + ")" : "";
        dialogPago.setHeaderText("Total a pagar: $" + prestamo.getMontoTotal() + mora);
        dialogPago.setContentText("Metodo de pago (RF-43):");

        dialogPago.showAndWait().ifPresent(metodo -> {
            try {
                pagoService.registrar(new PagoRequest(prestamo.getId(), metodo));
                String comprobante = pagoService.comprobante(prestamo.getId());
                Alert ventana = new Alert(Alert.AlertType.INFORMATION);
                ventana.setTitle("Comprobante ");
                ventana.setHeaderText("Pago registrado correctamente");
                TextArea area = new TextArea(comprobante);
                area.setEditable(false);
                area.setPrefSize(520, 320);
                ventana.getDialogPane().setContent(area);
                ventana.showAndWait();
            } catch (Exception e) {
                mostrarError("No se pudo registrar el pago");
            }
        });
    }

    private void mostrarErrorBusqueda(String mensaje) {
        labelErrorBusqueda.setText(mensaje);
        labelErrorBusqueda.setVisible(true);
    }

    @FXML
    private void handleDevolverAqui() {
        if (prestamoEncontrado == null) {
            mostrarErrorBusqueda("Primero busca un prestamo activo por placa");
            return;
        }
        devolverPrestamo(prestamoEncontrado);
    }

    private boolean coincidePrestamo(PrestamoResponse p, String texto) {
        if (texto == null || texto.isBlank()) return true;
        String f = texto.trim().toLowerCase();
        String cedula = p.getCliente() != null && p.getCliente().getCedula() != null
                ? p.getCliente().getCedula() : "";
        return p.getClienteNombre().toLowerCase().contains(f)
                || cedula.toLowerCase().contains(f)
                || p.getVehiculoPlacaTexto().toLowerCase().contains(f)
                || p.getEstadoTexto().toLowerCase().contains(f);
    }
}