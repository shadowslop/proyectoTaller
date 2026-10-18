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
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ReservaController {

    @FXML private ComboBox<ClienteDto> comboClienteReserva;
    @FXML private ComboBox<VehiculoDto> comboVehiculoReserva;
    @FXML private DatePicker fechaInicioReserva;
    @FXML private TextField horaInicioReserva;
    @FXML private DatePicker fechaFinReserva;
    @FXML private TextField horaFinReserva;
    @FXML private Button botonGuardarReserva;
    @FXML private Button botonCancelarEdicionReserva;
    @FXML private Label labelErrorReserva;

    @FXML private TableView<ReservaResponse> tablaReservas;
    @FXML private TableColumn<ReservaResponse, String> colClienteReserva;
    @FXML private TableColumn<ReservaResponse, String> colVehiculoReserva;
    @FXML private TableColumn<ReservaResponse, String> colInicioReserva;
    @FXML private TableColumn<ReservaResponse, String> colFinReserva;
    @FXML private TableColumn<ReservaResponse, String> colEstadoReserva;
    @FXML private TableColumn<ReservaResponse, Void> colAccionReserva;
    @FXML private TextField campoFiltroReservas;

    private UUID reservaEditandoId = null;

    private final ClienteService clienteService = new ClienteService();
    private final VehiculoService vehiculoService = new VehiculoService();
    private final ReservaService reservaService = new ReservaService();
    private final ObservableList<ReservaResponse> todasReservas = FXCollections.observableArrayList();
    private FilteredList<ReservaResponse> reservasFiltradas;

    // Listas fijas: se actualiza su CONTENIDO (setAll), nunca se reemplaza el objeto lista.
    private final ObservableList<ClienteDto> itemsClienteReserva = FXCollections.observableArrayList();
    private final ObservableList<VehiculoDto> itemsVehiculoReserva = FXCollections.observableArrayList();
    private final PrestamoService prestamoService = new PrestamoService();
    // Evita que un texto escrito por el usuario se pise con el texto que ponemos
    // nosotros mismos al seleccionar un item del combo.
    private boolean actualizandoTextoReserva = false;

    @FXML
    public void initialize() {
        colClienteReserva.setCellValueFactory(new PropertyValueFactory<>("clienteNombre"));
        colVehiculoReserva.setCellValueFactory(new PropertyValueFactory<>("vehiculoPlacaTexto"));
        colInicioReserva.setCellValueFactory(new PropertyValueFactory<>("fechaInicioTexto"));
        colFinReserva.setCellValueFactory(new PropertyValueFactory<>("fechaFinTexto"));
        colEstadoReserva.setCellValueFactory(new PropertyValueFactory<>("estado"));


        reservasFiltradas = new FilteredList<>(todasReservas, r -> true);
        tablaReservas.setItems(reservasFiltradas);
        campoFiltroReservas.textProperty().addListener((obs, a, texto) ->
                reservasFiltradas.setPredicate(r -> coincideReserva(r, texto)));
        agregarBotonesReserva();

        configurarComboClienteReserva();
        configurarComboVehiculoReserva();

        cargarReservas();
    }

    // ---------- Combo de cliente con autocompletado (reserva) ----------

    private void configurarComboClienteReserva() {
        comboClienteReserva.setItems(itemsClienteReserva);
        comboClienteReserva.setEditable(true);
        comboClienteReserva.setConverter(new StringConverter<>() {
            @Override
            public String toString(ClienteDto c) { return c == null ? "" : c.toString(); }
            @Override
            public ClienteDto fromString(String s) { return comboClienteReserva.getValue(); }
        });

        buscarClientes(itemsClienteReserva, comboClienteReserva, "");

        comboClienteReserva.getEditor().textProperty().addListener((obs, textoViejo, textoNuevo) -> {
            if (actualizandoTextoReserva) return;
            ClienteDto actual = comboClienteReserva.getValue();
            if (actual != null && actual.toString().equals(textoNuevo)) return;
            buscarClientes(itemsClienteReserva, comboClienteReserva, textoNuevo);
        });

        comboClienteReserva.setOnAction(e -> {
            ClienteDto seleccionado = comboClienteReserva.getValue();
            if (seleccionado != null) {
                actualizandoTextoReserva = true;
                comboClienteReserva.getEditor().setText(seleccionado.toString());
                actualizandoTextoReserva = false;
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
            mostrarErrorReserva("No se pudieron cargar los clientes");
        }
    }

    // ---------- Combo de vehiculo con autocompletado (reserva: TODOS los de la sede, no solo disponibles ahora) ----------
    // Una reserva es a futuro, asi que un vehiculo prestado hoy puede estar libre en la fecha de la reserva.
    // La disponibilidad real por fecha/hora la valida el backend al crear (RF-30).

    private void configurarComboVehiculoReserva() {
        comboVehiculoReserva.setItems(itemsVehiculoReserva);
        comboVehiculoReserva.setEditable(true);
        comboVehiculoReserva.setConverter(new StringConverter<>() {
            @Override
            public String toString(VehiculoDto v) { return v == null ? "" : v.toString(); }
            @Override
            public VehiculoDto fromString(String s) { return comboVehiculoReserva.getValue(); }
        });

        buscarVehiculosReserva("");

        comboVehiculoReserva.getEditor().textProperty().addListener((obs, textoViejo, textoNuevo) -> {
            if (actualizandoTextoReserva) return;
            VehiculoDto actual = comboVehiculoReserva.getValue();
            if (actual != null && actual.toString().equals(textoNuevo)) return;
            buscarVehiculosReserva(textoNuevo);
        });

        comboVehiculoReserva.setOnAction(e -> {
            VehiculoDto seleccionado = comboVehiculoReserva.getValue();
            if (seleccionado != null) {
                actualizandoTextoReserva = true;
                comboVehiculoReserva.getEditor().setText(seleccionado.toString());
                actualizandoTextoReserva = false;
            }
        });
    }

    private void buscarVehiculosReserva(String texto) {
        try {
            var todos = vehiculoService.listar();
            String filtro = texto == null ? "" : texto.trim().toLowerCase();
            var filtrados = todos.stream()
                    .filter(v -> filtro.isBlank()
                            || v.getPlaca().toLowerCase().contains(filtro)
                            || v.getTipoNombre().toLowerCase().contains(filtro))
                    .toList();
            Platform.runLater(() -> {
                itemsVehiculoReserva.setAll(filtrados);
                if (!filtrados.isEmpty() && !comboVehiculoReserva.isShowing()) {
                    comboVehiculoReserva.show();
                }
            });
        } catch (Exception e) {
            mostrarErrorReserva("No se pudieron cargar los vehiculos");
        }
    }

    private void cargarReservas() {
        try {
            var sedeActualId = SesionActual.getInstancia().getSedeId();
            todasReservas.setAll(reservaService.listarPorSede(sedeActualId));
        } catch (Exception e) {
            mostrarErrorReserva("No se pudieron cargar las reservas");
        }
    }

    @FXML
    private void handleAgendarReserva() {
        ClienteDto cliente = comboClienteReserva.getValue();
        VehiculoDto vehiculo = comboVehiculoReserva.getValue();
        var fechaIni = fechaInicioReserva.getValue();
        var fechaFin = fechaFinReserva.getValue();
        String horaIni = horaInicioReserva.getText();
        String horaFin = horaFinReserva.getText();

        boolean editando = reservaEditandoId != null;


        boolean faltaClienteVehiculo = !editando && (cliente == null || vehiculo == null);

        if (faltaClienteVehiculo || fechaIni == null || fechaFin == null
                || horaIni == null || horaIni.isBlank() || horaFin == null || horaFin.isBlank()) {
            mostrarErrorReserva(editando
                    ? "Completa fecha y hora de inicio y fin"
                    : "Completa cliente, vehiculo, fecha y hora de inicio y fin");
            return;
        }
        OffsetDateTime inicio;
        OffsetDateTime fin;
        try {
            inicio = combinarFechaHora(fechaIni, horaIni);
            fin = combinarFechaHora(fechaFin, horaFin);
        } catch (Exception e) {
            mostrarErrorReserva("La hora debe tener formato HH:mm (ej: 14:30)");
            return;
        }

        if (!fin.isAfter(inicio)) {
            mostrarErrorReserva("La fecha/hora de fin debe ser posterior a la de inicio");
            return;
        }

        var sesion = SesionActual.getInstancia();

        try {
            if (reservaEditandoId == null) {
                ReservaCreateRequest datos = new ReservaCreateRequest(
                        cliente.getCedula(), vehiculo.getPlaca(), sesion.getSedeId(), inicio, fin);
                reservaService.crear(datos);
            } else {
                ReservaActualizarRequest datos = new ReservaActualizarRequest(inicio, fin);
                reservaService.modificar(reservaEditandoId, datos);
            }
            limpiarFormularioReserva();
            cargarReservas();
        } catch (ApiException e) {
            mostrarErrorReserva(e.getMessage());
        } catch (Exception e) {
            mostrarErrorReserva(reservaEditandoId == null ? "No se pudo agendar la cita" : "No se pudo modificar la reserva");
        }
    }

    // "2026-09-21" + "14:30" -> OffsetDateTime en UTC. Mismo criterio que usas en DocumentoController con fechaVencimiento.
    private OffsetDateTime combinarFechaHora(java.time.LocalDate fecha, String horaTexto) {
        LocalTime hora = LocalTime.parse(horaTexto.trim(), DateTimeFormatter.ofPattern("H:mm"));
        return fecha.atTime(hora).atOffset(ZoneOffset.UTC);
    }

    @FXML
    private void handleCancelarEdicionReserva() {
        limpiarFormularioReserva();
    }

    private void agregarBotonesReserva() {
        colAccionReserva.setCellFactory(col -> new TableCell<>() {
            private final Button botonConfirmar = new Button("Confirmar");
            private final Button botonEditar = new Button("Modificar");
            private final Button botonCancelar = new Button("Cancelar");
            private final Button botonEliminar = new Button("Eliminar");
            private final Button botonEntregar = new Button("Entregar");
            private final HBox contenedor = new HBox(6, botonEntregar, botonConfirmar, botonEditar, botonCancelar, botonEliminar);

            {
                String estilo = "-fx-padding: 6 10 6 10; -fx-font-size: 12px;";
                botonConfirmar.setStyle(estilo);
                botonEditar.setStyle(estilo);
                botonCancelar.setStyle(estilo);
                botonEliminar.setStyle(estilo);
                botonEntregar.setStyle(estilo);


                botonConfirmar.setOnAction(e -> confirmarReserva(getTableView().getItems().get(getIndex())));
                botonEditar.setOnAction(e -> cargarParaEditarReserva(getTableView().getItems().get(getIndex())));
                botonCancelar.setOnAction(e -> cancelarReserva(getTableView().getItems().get(getIndex())));
                botonEliminar.setOnAction(e -> eliminarReserva(getTableView().getItems().get(getIndex())));
                botonEntregar.setOnAction(e -> entregarReserva(getTableView().getItems().get(getIndex())));
            }

            private void mostrar(Button boton, boolean visible) {
                boton.setVisible(visible);
                boton.setManaged(visible);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                ReservaResponse reserva = getTableView().getItems().get(getIndex());
                boolean pendiente = "PENDIENTE".equals(reserva.getEstado());
                boolean confirmada = "CONFIRMADA".equals(reserva.getEstado());
                boolean eliminable = reserva.puedeEliminarse();
                boolean activa = (pendiente || confirmada) && !eliminable;

                mostrar(botonConfirmar, pendiente && !eliminable);
                botonConfirmar.setDisable(!reserva.puedeConfirmarse());
                mostrar(botonEditar, activa);
                mostrar(botonCancelar, activa);
                mostrar(botonEliminar, eliminable);
                mostrar(botonEntregar, reserva.puedeEntregarse());

                setGraphic(contenedor);
            }
        });
    }
    private void cargarParaEditarReserva(ReservaResponse reserva) {
        reservaEditandoId = reserva.getId();

        actualizandoTextoReserva = true;
        comboClienteReserva.setValue(null);
        comboClienteReserva.getEditor().setText(reserva.getClienteNombre());
        comboVehiculoReserva.setValue(null);
        comboVehiculoReserva.getEditor().setText(reserva.getVehiculoPlacaTexto());
        actualizandoTextoReserva = false;

        comboClienteReserva.setDisable(true);
        comboVehiculoReserva.setDisable(true); // solo se permite cambiar fecha/hora al modificar (RF-32)

        fechaInicioReserva.setValue(reserva.getFechaInicio().toLocalDate());
        horaInicioReserva.setText(reserva.getFechaInicio().toLocalTime().withSecond(0).withNano(0).toString());
        fechaFinReserva.setValue(reserva.getFechaFin().toLocalDate());
        horaFinReserva.setText(reserva.getFechaFin().toLocalTime().withSecond(0).withNano(0).toString());

        botonGuardarReserva.setText("Guardar cambios");
        botonCancelarEdicionReserva.setVisible(true);
        botonCancelarEdicionReserva.setManaged(true);
        labelErrorReserva.setVisible(false);
    }

    private void cancelarReserva(ReservaResponse reserva) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Cancelar la reserva de " + reserva.getClienteNombre() + " para el vehiculo "
                        + reserva.getVehiculoPlacaTexto() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    reservaService.cancelar(reserva.getId());
                    if (reservaEditandoId != null && reservaEditandoId.equals(reserva.getId())) {
                        limpiarFormularioReserva();
                    }
                    cargarReservas();
                } catch (ApiException e) {
                    mostrarErrorReserva(e.getMessage());
                } catch (Exception e) {
                    mostrarErrorReserva("No se pudo cancelar la reserva");
                }
            }
        });
    }

    private void limpiarFormularioReserva() {
        reservaEditandoId = null;
        comboClienteReserva.setValue(null);
        comboClienteReserva.getEditor().clear();
        comboClienteReserva.setDisable(false);
        comboVehiculoReserva.setValue(null);
        comboVehiculoReserva.getEditor().clear();
        comboVehiculoReserva.setDisable(false);
        fechaInicioReserva.setValue(null);
        horaInicioReserva.clear();
        fechaFinReserva.setValue(null);
        horaFinReserva.clear();
        botonGuardarReserva.setText("Agendar cita");
        botonCancelarEdicionReserva.setVisible(false);
        botonCancelarEdicionReserva.setManaged(false);
        labelErrorReserva.setVisible(false);
    }

    private void mostrarErrorReserva(String mensaje) {
        labelErrorReserva.setText(mensaje);
        labelErrorReserva.setVisible(true);
    }

    private boolean coincideReserva(ReservaResponse r, String texto) {
        if (texto == null || texto.isBlank()) return true;
        String f = texto.trim().toLowerCase();
        String estado = r.getEstado() == null ? "" : r.getEstado();
        return r.getClienteNombre().toLowerCase().contains(f)
                || r.getVehiculoPlacaTexto().toLowerCase().contains(f)
                || estado.toLowerCase().contains(f);
    }
    private void confirmarReserva(ReservaResponse reserva) {
        try {
            reservaService.confirmar(reserva.getId());
            cargarReservas();
        } catch (ApiException e) {
            mostrarErrorReserva(e.getMessage());
        } catch (Exception e) {
            mostrarErrorReserva("No se pudo confirmar la reserva");
        }
    }
    private void eliminarReserva(ReservaResponse reserva) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar definitivamente la reserva de " + reserva.getClienteNombre()
                        + " para el vehiculo " + reserva.getVehiculoPlacaTexto() + "? Esta accion no se puede deshacer.",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    reservaService.eliminar(reserva.getId());
                    if (reservaEditandoId != null && reservaEditandoId.equals(reserva.getId())) {
                        limpiarFormularioReserva();
                    }
                    cargarReservas();
                } catch (ApiException e) {
                    mostrarErrorReserva(e.getMessage());
                } catch (Exception e) {
                    mostrarErrorReserva("No se pudo eliminar la reserva");
                }
            }
        });
    }
    private void entregarReserva(ReservaResponse reserva) {
        try {
            var sesion = SesionActual.getInstancia();
            var prestamo = prestamoService.crearDesdeReserva(reserva.getId(), sesion.getUsuarioId());

            TextInputDialog dKm = new TextInputDialog();
            dKm.setTitle("Entrega de vehiculo");
            dKm.setHeaderText("Kilometraje actual del vehiculo:");
            dKm.setContentText("Km inicial:");
            Integer kmIni = parseEntero(dKm.showAndWait().orElse(null));

            TextInputDialog dComb = new TextInputDialog();
            dComb.setHeaderText("Nivel de combustible:");
            dComb.setContentText("Combustible inicial (%):");
            Integer combIni = parseEntero(dComb.showAndWait().orElse(null));

            if (kmIni != null || combIni != null) {
                prestamoService.registrarEntrega(prestamo.getId(), new EntregaRequest(kmIni, combIni));
            }
            cargarReservas();
        } catch (ApiException e) {
            mostrarErrorReserva(e.getMessage());
        } catch (Exception e) {
            mostrarErrorReserva("No se pudo entregar la reserva como prestamo");
        }
    }

    private Integer parseEntero(String texto) {
        try { return Integer.valueOf(texto.trim()); } catch (Exception e) { return null; }
    }
}