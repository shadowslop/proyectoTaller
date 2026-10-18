package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.AuditoriaDto;
import com.tallerfrontend.frontend.service.AuditoriaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AuditoriaController {

    @FXML private TextField campoBuscar;
    @FXML private Label labelError;

    @FXML private TableView<AuditoriaDto> tablaAuditoria;
    @FXML private TableColumn<AuditoriaDto, String> colFecha;
    @FXML private TableColumn<AuditoriaDto, String> colUsuario;
    @FXML private TableColumn<AuditoriaDto, String> colAccion;
    @FXML private TableColumn<AuditoriaDto, String> colEntidad;
    @FXML private TableColumn<AuditoriaDto, String> colDetalle;

    private final AuditoriaService auditoriaService = new AuditoriaService();
    private final ObservableList<AuditoriaDto> todos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaTexto"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colAccion.setCellValueFactory(new PropertyValueFactory<>("accion"));
        colEntidad.setCellValueFactory(new PropertyValueFactory<>("entidad"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));

        cargarAuditoria();

        campoBuscar.textProperty().addListener((obs, viejo, nuevo) -> aplicarFiltro(nuevo));
    }

    private void cargarAuditoria() {
        try {
            var lista = auditoriaService.listar();
            // Mas reciente primero (RF-49: fecha, hora y usuario responsable)
            lista.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));
            todos.setAll(lista);
            tablaAuditoria.setItems(todos);
            labelError.setVisible(false);
        } catch (Exception e) {
            mostrarError("No se pudo cargar la bitacora de auditoria");
        }
    }

    private void aplicarFiltro(String texto) {
        if (texto == null || texto.isBlank()) {
            tablaAuditoria.setItems(todos);
            return;
        }
        String filtro = texto.trim().toLowerCase();
        ObservableList<AuditoriaDto> filtrados = FXCollections.observableArrayList(
                todos.stream().filter(a ->
                        (a.getUsuario() != null && a.getUsuario().toLowerCase().contains(filtro)) ||
                                (a.getAccion() != null && a.getAccion().toLowerCase().contains(filtro)) ||
                                (a.getEntidad() != null && a.getEntidad().toLowerCase().contains(filtro)) ||
                                (a.getDetalle() != null && a.getDetalle().toLowerCase().contains(filtro))
                ).toList()
        );
        tablaAuditoria.setItems(filtrados);
    }

    @FXML
    private void handleRefrescar() {
        campoBuscar.clear();
        cargarAuditoria();
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }
}