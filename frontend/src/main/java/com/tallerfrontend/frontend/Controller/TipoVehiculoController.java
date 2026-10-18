package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.TipoVehiculoDto;
import com.tallerfrontend.frontend.service.ApiException;
import com.tallerfrontend.frontend.service.TipoVehiculoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.util.UUID;

public class TipoVehiculoController {

    @FXML private TextField campoNombre;
    @FXML private TextField campoPrecio;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelarEdicion;
    @FXML private Label labelError;

    @FXML private TableView<TipoVehiculoDto> tablaTipos;
    @FXML private TableColumn<TipoVehiculoDto, String> colNombre;
    @FXML private TableColumn<TipoVehiculoDto, BigDecimal> colPrecio;
    @FXML private TableColumn<TipoVehiculoDto, String> colActivo;
    @FXML private TableColumn<TipoVehiculoDto, Void> colAccion;

    private final TipoVehiculoService tipoVehiculoService = new TipoVehiculoService();

    // Cuando no es null, "Crear tipo" en realidad esta editando este tipo.
    private UUID tipoEditandoId = null;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioDia"));
        colActivo.setCellValueFactory(cellData -> {
            Boolean activo = cellData.getValue().getActivo();
            String texto = Boolean.TRUE.equals(activo) ? "Activo" : "Inactivo";
            return new javafx.beans.property.SimpleStringProperty(texto);
        });

        agregarBotonesAccion();
        cargarTipos();
    }

    private void cargarTipos() {
        try {
            tablaTipos.setItems(FXCollections.observableArrayList(tipoVehiculoService.listarTodos()));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los tipos de vehiculo");
        }
    }

    @FXML
    private void handleGuardar() {
        String nombre = campoNombre.getText();
        String precioTexto = campoPrecio.getText();

        if (nombre == null || nombre.isBlank() || precioTexto == null || precioTexto.isBlank()) {
            mostrarError("Completa el nombre y el precio");
            return;
        }

        BigDecimal precio;
        try {
            precio = parsearPrecio(precioTexto);
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un numero valido");
            return;
        }

        boolean estabaEditando = tipoEditandoId != null;

        try {
            if (!estabaEditando) {
                tipoVehiculoService.crear(nombre.trim(), precio);
            } else {
                tipoVehiculoService.actualizar(tipoEditandoId, nombre.trim(), precio);
            }
            limpiarFormulario();
            cargarTipos();
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError(estabaEditando ? "No se pudo actualizar el tipo" : "No se pudo crear el tipo");
        }
    }

    // "120.000" o "120,000" -> 120000. Mismo criterio que ya usas en VehiculoController.
    private BigDecimal parsearPrecio(String texto) {
        String soloDigitos = texto.replaceAll("[^0-9]", "");
        if (soloDigitos.isBlank()) {
            throw new NumberFormatException("Precio vacio");
        }
        return new BigDecimal(soloDigitos);
    }

    @FXML
    private void handleCancelarEdicion() {
        limpiarFormulario();
    }

    private void agregarBotonesAccion() {
        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button botonEditar = new Button("Editar precio");
            private final Button botonDesactivar = new Button("Desactivar");
            private final HBox contenedor = new HBox(6, botonEditar, botonDesactivar);

            {
                botonEditar.setStyle("-fx-padding: 6 10 6 10; -fx-font-size: 12px;");
                botonDesactivar.setStyle("-fx-padding: 6 10 6 10; -fx-font-size: 12px;");

                botonEditar.setOnAction(e -> {
                    TipoVehiculoDto tipo = getTableView().getItems().get(getIndex());
                    cargarParaEditar(tipo);
                });
                botonDesactivar.setOnAction(e -> {
                    TipoVehiculoDto tipo = getTableView().getItems().get(getIndex());
                    desactivarTipo(tipo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    private void cargarParaEditar(TipoVehiculoDto tipo) {
        tipoEditandoId = tipo.getId();
        campoNombre.setText(tipo.getNombre());
        campoPrecio.setText(tipo.getPrecioDia() != null ? tipo.getPrecioDia().toPlainString() : "");
        botonGuardar.setText("Guardar cambios");
        botonCancelarEdicion.setVisible(true);
        botonCancelarEdicion.setManaged(true);
        labelError.setVisible(false);
    }

    private void desactivarTipo(TipoVehiculoDto tipo) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desactivar el tipo \"" + tipo.getNombre() + "\"? Ya no aparecera al crear vehiculos nuevos.",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    tipoVehiculoService.desactivar(tipo.getId());
                    cargarTipos();
                } catch (ApiException e) {
                    mostrarError(e.getMessage());
                } catch (Exception e) {
                    mostrarError("No se pudo desactivar el Tipo");
                }
            }
        });
    }

    private void limpiarFormulario() {
        tipoEditandoId = null;
        campoNombre.clear();
        campoPrecio.clear();
        botonGuardar.setText("Crear tipo");
        botonCancelarEdicion.setVisible(false);
        botonCancelarEdicion.setManaged(false);
        labelError.setVisible(false);
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }
}