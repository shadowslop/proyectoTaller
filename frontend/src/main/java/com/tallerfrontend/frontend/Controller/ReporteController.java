package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.*;
import com.tallerfrontend.frontend.service.ReporteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

public class ReporteController {

    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private Label labelError;
    @FXML private Label labelTotal;
    @FXML private Label labelActivos;
    @FXML private Label labelDevueltos;
    @FXML private Label labelMonto;

    @FXML private TableView<ReporteResponse.ReporteSede> tablaPorSede;
    @FXML private TableColumn<ReporteResponse.ReporteSede, String> colSedeNombre;
    @FXML private TableColumn<ReporteResponse.ReporteSede, Long> colSedeCantidad;
    @FXML private TableColumn<ReporteResponse.ReporteSede, java.math.BigDecimal> colSedeMonto;

    @FXML private TableView<ReporteResponse.ReporteTipoVehiculo> tablaPorTipo;
    @FXML private TableColumn<ReporteResponse.ReporteTipoVehiculo, String> colTipoNombre;
    @FXML private TableColumn<ReporteResponse.ReporteTipoVehiculo, Long> colTipoCantidad;
    @FXML private TableColumn<ReporteResponse.ReporteTipoVehiculo, java.math.BigDecimal> colTipoMonto;

    // ---------- Agenda ----------
    @FXML private Label labelAgendaTotal;
    @FXML private Label labelAgendaConfirmadas;
    @FXML private Label labelAgendaPendientes;
    @FXML private Label labelAgendaFinalizadas;
    @FXML private Label labelAgendaCanceladas;

    @FXML private TableView<ReporteAgendaResponse.ReporteAgendaItem> tablaAgendaPorDia;
    @FXML private TableColumn<ReporteAgendaResponse.ReporteAgendaItem, String> colAgendaDiaEtiqueta;
    @FXML private TableColumn<ReporteAgendaResponse.ReporteAgendaItem, Long> colAgendaDiaCantidad;

    @FXML private TableView<ReporteAgendaResponse.ReporteAgendaItem> tablaAgendaPorMes;
    @FXML private TableColumn<ReporteAgendaResponse.ReporteAgendaItem, String> colAgendaMesEtiqueta;
    @FXML private TableColumn<ReporteAgendaResponse.ReporteAgendaItem, Long> colAgendaMesCantidad;

    // ---------- Estado de la flota (RF-47) ----------
    @FXML private Label labelCantDisponibles;
    @FXML private Label labelCantAlquilados;
    @FXML private Label labelCantMantenimiento;

    @FXML private TableView<VehiculoReporteDto> tablaVehiculosEstado;
    @FXML private TableColumn<VehiculoReporteDto, String> colVehEstadoPlaca;
    @FXML private TableColumn<VehiculoReporteDto, String> colVehEstadoTipo;
    @FXML private TableColumn<VehiculoReporteDto, String> colVehEstadoSituacion;

    private final ReporteService reporteService = new ReporteService();

    // Como VehiculoDto no trae texto de "situacion", lo guardamos aparte
    // con un mapa Vehiculo->texto y una columna que lo consulta.
    private final java.util.Map<VehiculoReporteDto, String> situacionPorVehiculo = new java.util.HashMap<>();

    @FXML
    public void initialize() {
        colSedeNombre.setCellValueFactory(new PropertyValueFactory<>("sedeNombre"));
        colSedeCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadPrestamos"));
        colSedeMonto.setCellValueFactory(new PropertyValueFactory<>("montoRecaudado"));

        colTipoNombre.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipoCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadPrestamos"));
        colTipoMonto.setCellValueFactory(new PropertyValueFactory<>("montoRecaudado"));

        colAgendaDiaEtiqueta.setCellValueFactory(new PropertyValueFactory<>("etiqueta"));
        colAgendaDiaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colAgendaMesEtiqueta.setCellValueFactory(new PropertyValueFactory<>("etiqueta"));
        colAgendaMesCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        colVehEstadoPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colVehEstadoTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colVehEstadoSituacion.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        situacionPorVehiculo.getOrDefault(cellData.getValue(), "")));

        cargarReporteVehiculos();
    }

    @FXML private void handleHoy() {
        LocalDate hoy = LocalDate.now();
        fechaInicio.setValue(hoy);
        fechaFin.setValue(hoy);
        generar();
    }

    @FXML private void handleSemana() {
        LocalDate hoy = LocalDate.now();
        fechaInicio.setValue(hoy.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)));
        fechaFin.setValue(hoy.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)));
        generar();
    }

    @FXML private void handleQuincena() {
        LocalDate hoy = LocalDate.now();
        if (hoy.getDayOfMonth() <= 15) {
            fechaInicio.setValue(hoy.withDayOfMonth(1));
            fechaFin.setValue(hoy.withDayOfMonth(15));
        } else {
            fechaInicio.setValue(hoy.withDayOfMonth(16));
            fechaFin.setValue(hoy.with(TemporalAdjusters.lastDayOfMonth()));
        }
        generar();
    }

    @FXML private void handleMes() {
        LocalDate hoy = LocalDate.now();
        fechaInicio.setValue(hoy.with(TemporalAdjusters.firstDayOfMonth()));
        fechaFin.setValue(hoy.with(TemporalAdjusters.lastDayOfMonth()));
        generar();
    }

    @FXML
    private void handleGenerar() {
        if (fechaInicio.getValue() == null || fechaFin.getValue() == null) {
            mostrarError("Selecciona ambas fechas");
            return;
        }
        generar();
    }

    private void generar() {
        try {
            OffsetDateTime inicio = fechaInicio.getValue().atStartOfDay().atOffset(ZoneOffset.of("-05:00"));
            OffsetDateTime fin = fechaFin.getValue().atTime(23, 59, 59).atOffset(ZoneOffset.of("-05:00"));

            ReporteResponse reporte = reporteService.generar(inicio, fin);

            labelTotal.setText(String.valueOf(reporte.getTotalPrestamos()));
            labelActivos.setText(String.valueOf(reporte.getPrestamosActivos()));
            labelDevueltos.setText(String.valueOf(reporte.getPrestamosDevueltos()));
            labelMonto.setText(reporte.getMontoTotalRecaudado().toString());

            tablaPorSede.setItems(FXCollections.observableArrayList(reporte.getPorSede()));
            tablaPorTipo.setItems(FXCollections.observableArrayList(reporte.getPorTipoVehiculo()));

            ReporteAgendaResponse agenda = reporteService.generarAgenda(inicio, fin);

            labelAgendaTotal.setText(String.valueOf(agenda.getTotalReservas()));
            labelAgendaConfirmadas.setText(String.valueOf(agenda.getConfirmadas()));
            labelAgendaPendientes.setText(String.valueOf(agenda.getPendientes()));
            labelAgendaFinalizadas.setText(String.valueOf(agenda.getFinalizadas()));
            labelAgendaCanceladas.setText(String.valueOf(agenda.getCanceladas()));

            tablaAgendaPorDia.setItems(FXCollections.observableArrayList(agenda.getPorDiaSemana()));
            tablaAgendaPorMes.setItems(FXCollections.observableArrayList(agenda.getPorMes()));

            labelError.setVisible(false);
        } catch (Exception e) {
            mostrarError("No se pudo generar el reporte");
        }
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }

    @FXML
    private void handleRefrescarVehiculos() {
        cargarReporteVehiculos();
    }

    private void cargarReporteVehiculos() {
        try {
            situacionPorVehiculo.clear(); // evita arrastrar situaciones viejas entre refrescos

            ReporteVehiculosResponse reporte = reporteService.reporteVehiculos();

            labelCantDisponibles.setText(String.valueOf(reporte.getDisponibles().size()));
            labelCantAlquilados.setText(String.valueOf(reporte.getAlquilados().size()));
            labelCantMantenimiento.setText(String.valueOf(reporte.getEnMantenimiento().size()));

            ObservableList<VehiculoReporteDto> filaUnica = FXCollections.observableArrayList();
            agregarConEstado(filaUnica, reporte.getDisponibles(), "Disponible");
            agregarConEstado(filaUnica, reporte.getAlquilados(), "Alquilado");
            agregarConEstado(filaUnica, reporte.getEnMantenimiento(), "En mantenimiento");

            tablaVehiculosEstado.setItems(filaUnica);
        } catch (Exception e) {
            mostrarError("No se pudo cargar el reporte de vehiculos");
        }
    }
    private void agregarConEstado(ObservableList<VehiculoReporteDto> destino,
                                  java.util.List<VehiculoReporteDto> lista, String situacion) {
        for (VehiculoReporteDto v : lista) {
            situacionPorVehiculo.put(v, situacion);
            destino.add(v);
        }
    }
}