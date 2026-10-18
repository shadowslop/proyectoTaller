package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.DocumentoDto;
import com.tallerfrontend.frontend.model.DocumentoResponse;
import com.tallerfrontend.frontend.model.VehiculoDto;
import com.tallerfrontend.frontend.service.ApiException;
import com.tallerfrontend.frontend.service.DocumentoService;
import com.tallerfrontend.frontend.service.VehiculoService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class DocumentoController {

    @FXML private ComboBox<VehiculoDto> comboVehiculo;
    @FXML private ComboBox<String> comboTipoDocumento;
    @FXML private ComboBox<String> comboEmpresa;
    @FXML private DatePicker fechaVencimiento;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelarEdicion;
    @FXML private Label labelError;

    @FXML private ComboBox<VehiculoDto> comboPlacaBusqueda;

    @FXML private TableView<DocumentoResponse> tablaDocumentos;
    @FXML private TableColumn<DocumentoResponse, String> colPlaca;
    @FXML private TableColumn<DocumentoResponse, String> colTipo;
    @FXML private TableColumn<DocumentoResponse, String> colEmpresa;
    @FXML private TableColumn<DocumentoResponse, String> colVencimiento;
    @FXML private TableColumn<DocumentoResponse, Void> colAccion;

    private final DocumentoService documentoService = new DocumentoService();
    private final VehiculoService vehiculoService = new VehiculoService();

    // Listas fijas: se actualiza su CONTENIDO (setAll), nunca se reemplaza el objeto lista.
    // Cada combo tiene su propia lista para que buscar en uno no le mueva el popup al otro.
    private final ObservableList<VehiculoDto> itemsVehiculo = FXCollections.observableArrayList();
    private final ObservableList<VehiculoDto> itemsVehiculoBusqueda = FXCollections.observableArrayList();

    // Empresas sugeridas en el combo. Es mutable: el usuario puede eliminar una sugerencia
    // desde el popup, y si escribe una empresa nueva al crear un documento, se agrega aqui.
    private final ObservableList<String> itemsEmpresa = FXCollections.observableArrayList(
            "Seguros Bolivar", "Seguros SURA", "Seguros Mundial", "CDA Diagnosticentro"
    );

    // Cuando no es null, "Crear documento" en realidad esta editando este documento.
    private UUID documentoEditandoId = null;

    private boolean actualizandoTextoProgramaticamente = false;
    private boolean actualizandoTextoBusqueda = false;

    @FXML
    public void initialize() {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("vehiculoPlacaTexto"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimientoTexto"));

        comboTipoDocumento.setItems(FXCollections.observableArrayList(
                "SOAT", "TECNOMECANICA", "SEGURO_TODO_RIESGO", "IMPUESTOS"));

        comboEmpresa.setEditable(true);
        comboEmpresa.setItems(itemsEmpresa);
        configurarCellFactoryEmpresa();

        configurarComboVehiculo();
        configurarComboBusqueda();
        agregarBotonesAccion();

        cargarVencidos();
    }

    // ---------- Combo de empresa con opcion de eliminar sugerencias ----------

    private void configurarCellFactoryEmpresa() {
        comboEmpresa.setCellFactory(listView -> new ListCell<>() {
            private final Label texto = new Label();
            private final Button botonEliminar = new Button("✕");
            private final HBox contenedor = new HBox(texto, botonEliminar);

            {
                HBox.setHgrow(texto, Priority.ALWAYS);
                texto.setMaxWidth(Double.MAX_VALUE);
                contenedor.setSpacing(8);
                contenedor.setAlignment(Pos.CENTER_LEFT);
                botonEliminar.setStyle("-fx-padding: 0 6 0 6; -fx-font-size: 11px; -fx-text-fill: red; -fx-background-color: transparent;");

                botonEliminar.setOnAction(e -> {
                    String valor = getItem();
                    if (valor != null) {
                        itemsEmpresa.remove(valor);
                        if (valor.equals(comboEmpresa.getEditor().getText())) {
                            comboEmpresa.getEditor().clear();
                            comboEmpresa.setValue(null);
                        }
                    }
                    comboEmpresa.hide();
                    Platform.runLater(comboEmpresa::show); // reabre el popup ya sin el item eliminado
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    texto.setText(item);
                    setGraphic(contenedor);
                }
            }
        });
    }

    // ---------- Combo de vehiculo con autocompletado (para crear/editar documento) ----------

    private void configurarComboVehiculo() {
        comboVehiculo.setItems(itemsVehiculo);
        comboVehiculo.setEditable(true);
        comboVehiculo.setConverter(new StringConverter<>() {
            @Override
            public String toString(VehiculoDto v) { return v == null ? "" : v.toString(); }
            @Override
            public VehiculoDto fromString(String s) { return comboVehiculo.getValue(); }
        });

        buscarVehiculos(itemsVehiculo, "");

        // Si el texto nuevo es exactamente el toString() del vehiculo ya seleccionado,
        // el cambio vino de la propia seleccion (JavaFX escribe el texto despues de fijar
        // el value) y no de que el usuario este escribiendo: no hay que volver a buscar,
        // o se vacia la lista justo despues de seleccionar y parece que el clic "no hizo nada".
        comboVehiculo.getEditor().textProperty().addListener((obs, textoViejo, textoNuevo) -> {
            if (actualizandoTextoProgramaticamente) return;
            VehiculoDto actual = comboVehiculo.getValue();
            if (actual != null && actual.toString().equals(textoNuevo)) return;
            buscarVehiculos(itemsVehiculo, textoNuevo);
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

    // ---------- Combo de vehiculo con autocompletado (para buscar documentos por vehiculo) ----------

    private void configurarComboBusqueda() {
        comboPlacaBusqueda.setItems(itemsVehiculoBusqueda);
        comboPlacaBusqueda.setEditable(true);
        comboPlacaBusqueda.setConverter(new StringConverter<>() {
            @Override
            public String toString(VehiculoDto v) { return v == null ? "" : v.toString(); }
            @Override
            public VehiculoDto fromString(String s) { return comboPlacaBusqueda.getValue(); }
        });

        buscarVehiculos(itemsVehiculoBusqueda, "");

        comboPlacaBusqueda.getEditor().textProperty().addListener((obs, textoViejo, textoNuevo) -> {
            if (actualizandoTextoBusqueda) return;
            VehiculoDto actual = comboPlacaBusqueda.getValue();
            if (actual != null && actual.toString().equals(textoNuevo)) return;
            buscarVehiculos(itemsVehiculoBusqueda, textoNuevo);
        });

        comboPlacaBusqueda.setOnAction(e -> {
            VehiculoDto seleccionado = comboPlacaBusqueda.getValue();
            if (seleccionado != null) {
                actualizandoTextoBusqueda = true;
                comboPlacaBusqueda.getEditor().setText(seleccionado.toString());
                actualizandoTextoBusqueda = false;
            }
        });
    }

    // Aqui se listan TODOS los vehiculos (no solo los disponibles), porque tanto para
    // registrar un documento como para consultar su historial puede ser un vehiculo prestado.
    private void buscarVehiculos(ObservableList<VehiculoDto> destino, String texto) {
        try {
            var todos = vehiculoService.listar();
            String filtro = texto == null ? "" : texto.trim().toLowerCase();
            var filtrados = todos.stream()
                    .filter(v -> filtro.isBlank()
                            || v.getPlaca().toLowerCase().contains(filtro)
                            ||v.getTipoNombre().toLowerCase().contains(filtro))
                    .toList();

            ComboBox<VehiculoDto> combo = destino == itemsVehiculo ? comboVehiculo : comboPlacaBusqueda;
            Platform.runLater(() -> {
                destino.setAll(filtrados);
                if (!filtrados.isEmpty() && !combo.isShowing()) {
                    combo.show();
                }
            });
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los vehiculos");
        }
    }

    private void cargarVencidos() {
        try {
            tablaDocumentos.setItems(FXCollections.observableArrayList(documentoService.listarVencidos()));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los documentos vencidos");
        }
    }

    @FXML
    private void handleVerVencidos() {
        cargarVencidos();
        labelError.setVisible(false);
    }

    @FXML
    private void handleBuscarPorVehiculo() {
        VehiculoDto vehiculo = comboPlacaBusqueda.getValue();

        if (vehiculo == null) {
            mostrarError("Selecciona un vehiculo de la lista para buscar");
            return;
        }

        try {
            tablaDocumentos.setItems(FXCollections.observableArrayList(documentoService.listarPorVehiculo(vehiculo.getPlaca())));
            labelError.setVisible(false);
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los documentos de ese vehiculo");
        }
    }

    @FXML
    private void handleCrear() {
        VehiculoDto vehiculo = comboVehiculo.getValue();
        String tipo = comboTipoDocumento.getValue();
        var fecha = fechaVencimiento.getValue();
        String empresa = comboEmpresa.getEditor().getText();

        if (vehiculo == null || tipo == null || fecha == null) {
            mostrarError("Selecciona el vehiculo de la lista, el tipo y la fecha");
            return;
        }
        if (empresa == null || empresa.isBlank()) {
            mostrarError("Ingresa o selecciona la empresa que emite el documento");
            return;
        }
        empresa = empresa.trim();

        if (!itemsEmpresa.contains(empresa)) {
            itemsEmpresa.add(empresa);
        }

        OffsetDateTime fechaVencimientoValor = fecha.atStartOfDay().atOffset(ZoneOffset.UTC);
        DocumentoDto datos = new DocumentoDto(vehiculo.getPlaca(), tipo, fechaVencimientoValor, empresa);

        boolean estabaEditando = documentoEditandoId != null;
        String placaEditada = vehiculo.getPlaca();

        try {
            if (!estabaEditando) {
                documentoService.crear(datos);
            } else {
                documentoService.actualizar(documentoEditandoId, datos);
            }
            limpiarFormulario();

            if (estabaEditando) {
                tablaDocumentos.setItems(FXCollections.observableArrayList(documentoService.listarPorVehiculo(placaEditada)));
                mostrarExito("Documento actualizado correctamente");
            } else {
                cargarVencidos();
            }
        } catch (ApiException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError(estabaEditando ? "No se pudo actualizar el documento" : "No se pudo crear el documento");
        }
    }

    @FXML
    private void handleCancelarEdicion() {
        limpiarFormulario();
    }

    // ---------- Editar / Eliminar desde la tabla ----------

    private void agregarBotonesAccion() {
        colAccion.setCellFactory(col -> new TableCell<>() {
            private final Button botonEditar = new Button("Editar");
            private final Button botonEliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(6, botonEditar, botonEliminar);

            {
                botonEditar.setStyle("-fx-padding: 6 14 6 14; -fx-font-size: 13px;");
                botonEliminar.setStyle("-fx-padding: 6 14 6 14; -fx-font-size: 13px;");

                botonEditar.setOnAction(e -> {
                    DocumentoResponse documento = getTableView().getItems().get(getIndex());
                    cargarParaEditar(documento);
                });
                botonEliminar.setOnAction(e -> {
                    DocumentoResponse documento = getTableView().getItems().get(getIndex());
                    eliminarDocumento(documento);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    private void cargarParaEditar(DocumentoResponse documento) {
        documentoEditandoId = documento.getId();

        actualizandoTextoProgramaticamente = true;
        comboVehiculo.setValue(null);
        comboVehiculo.getEditor().setText(documento.getVehiculoPlacaTexto());
        actualizandoTextoProgramaticamente = false;
        buscarVehiculos(itemsVehiculo, documento.getVehiculoPlacaTexto());
        seleccionarVehiculoPorPlaca(documento.getVehiculoId());

        comboTipoDocumento.setValue(documento.getTipoDocumento());
        comboEmpresa.setValue(documento.getEmpresa());
        comboEmpresa.getEditor().setText(documento.getEmpresa() == null ? "" : documento.getEmpresa());
        fechaVencimiento.setValue(documento.getFechaVencimiento().toLocalDate());

        botonGuardar.setText("Guardar cambios");
        botonCancelarEdicion.setVisible(true);
        botonCancelarEdicion.setManaged(true);
        labelError.setVisible(false);
    }

    // El combo necesita el VehiculoDto seleccionado (no solo el texto) para poder editar
    // el tipo y no perder la referencia al vehiculo si el usuario le da "Guardar cambios" sin retocar el texto.
    private void seleccionarVehiculoPorPlaca(VehiculoDto vehiculo) {
        if (vehiculo == null) return;
        itemsVehiculo.stream()
                .filter(v -> v.getPlaca().equals(vehiculo.getPlaca()))
                .findFirst()
                .ifPresentOrElse(
                        comboVehiculo::setValue,
                        () -> {
                            itemsVehiculo.add(0, vehiculo);
                            comboVehiculo.setValue(vehiculo);
                        }
                );
    }

    private void eliminarDocumento(DocumentoResponse documento) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el documento " + documento.getTipoDocumento() + " del vehiculo "
                        + documento.getVehiculoPlacaTexto() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    documentoService.eliminar(documento.getId());
                    cargarVencidos();
                } catch (ApiException e) {
                    mostrarError(e.getMessage());
                } catch (Exception e) {
                    mostrarError("No se pudo eliminar el documento");
                }
            }
        });
    }

    private void limpiarFormulario() {
        documentoEditandoId = null;
        comboVehiculo.setValue(null);
        comboVehiculo.getEditor().clear();
        comboTipoDocumento.setValue(null);
        comboEmpresa.setValue(null);
        comboEmpresa.getEditor().clear();
        fechaVencimiento.setValue(null);
        botonGuardar.setText("Crear documento");
        botonCancelarEdicion.setVisible(false);
        botonCancelarEdicion.setManaged(false);
        labelError.setVisible(false);
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
    @FXML
    private void handleVerPorVencer() {
        try {
            tablaDocumentos.setItems(FXCollections.observableArrayList(documentoService.listarPorVencer()));
            labelError.setVisible(false);
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los documentos por vencer");
        }
    }
}