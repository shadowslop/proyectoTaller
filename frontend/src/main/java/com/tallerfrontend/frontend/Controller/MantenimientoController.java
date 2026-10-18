package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.MantenimientoDto;
import com.tallerfrontend.frontend.model.VehiculoDto;
import com.tallerfrontend.frontend.service.ApiException;
import com.tallerfrontend.frontend.service.MantenimientoService;
import com.tallerfrontend.frontend.service.VehiculoService;
import com.tallerfrontend.frontend.util.SesionActual;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class MantenimientoController {

    @FXML private ComboBox<VehiculoDto> comboVehiculo;
    @FXML private ComboBox<String> comboTipo;
    @FXML private DatePicker campoFecha;
    @FXML private Button botonAgendar;
    @FXML private Label labelError;

    @FXML private TableView<MantenimientoDto> tablaMantenimientos;
    @FXML private TableColumn<MantenimientoDto, String> colVehiculo;
    @FXML private TableColumn<MantenimientoDto, String> colTipoVehiculo;
    @FXML private TableColumn<MantenimientoDto, String> colTipoMantenimiento;
    @FXML private TableColumn<MantenimientoDto, LocalDate> colFecha;
    @FXML private TableColumn<MantenimientoDto, String> colBloqueaHoy;
    @FXML private TableColumn<MantenimientoDto, Void> colAccion;

    private final MantenimientoService mantenimientoService = new MantenimientoService();
    private final VehiculoService vehiculoService = new VehiculoService();

    @FXML
    public void initialize() {
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("vehiculoPlaca"));
        colTipoVehiculo.setCellValueFactory(new PropertyValueFactory<>("vehiculoTipo"));
        colTipoMantenimiento.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colBloqueaHoy.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isBloqueaHoy() ? "Si (bloqueado hoy)" : "No"));

        comboTipo.setItems(FXCollections.observableArrayList("PREVENTIVO", "ARREGLO"));
        campoFecha.setValue(LocalDate.now());

        // Agendar y cancelar solo los puede hacer SUPERADMIN
        boolean esSuperAdmin = "SUPERADMIN".equals(SesionActual.getInstancia().getRol());
        botonAgendar.setVisible(esSuperAdmin);
        botonAgendar.setManaged(esSuperAdmin);
        agregarBotonCancelar(esSuperAdmin);

        cargarVehiculos();
        cargarMantenimientos();
    }

    private void cargarVehiculos() {
        try {
            comboVehiculo.setItems(FXCollections.observableArrayList(vehiculoService.listar()));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los vehiculos");
        }
    }

    private void cargarMantenimientos() {
        try {
            tablaMantenimientos.setItems(FXCollections.observableArrayList(mantenimientoService.listar()));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los mantenimientos");
        }
    }

    @FXML
    private void handleAgendar() {
        VehiculoDto vehiculo = comboVehiculo.getValue();
        String tipo = comboTipo.getValue();
        LocalDate fecha = campoFecha.getValue();

        if (vehiculo == null || tipo == null || fecha == null) {
            mostrarError("Completa todos los campos");
            return;
        }

        try {
            mantenimientoService.agendar(vehiculo.getPlaca(), tipo, fecha);
            comboTipo.setValue(null);
            campoFecha.setValue(LocalDate.now());
            labelError.setVisible(false);
            cargarMantenimientos();
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("No se pudo agendar el mantenimiento");
        }
    }

    private void agregarBotonCancelar(boolean esSuperAdmin) {
        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button boton = new Button("Cancelar");
            {
                boton.setOnAction(e -> {
                    MantenimientoDto m = getTableView().getItems().get(getIndex());
                    cancelarMantenimiento(m);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || !esSuperAdmin ? null : boton);
            }
        });
    }

    private void cancelarMantenimiento(MantenimientoDto m) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Cancelar el mantenimiento del vehiculo " + m.getVehiculoPlaca()
                        + " programado para el " + m.getFecha() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    mantenimientoService.cancelar(m.getId());
                    cargarMantenimientos();
                } catch (ApiException e) {
                    mostrarError(e.getMessage());
                } catch (Exception e) {
                    mostrarError("No se pudo cancelar el mantenimiento");
                }
            }
        });
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }
}