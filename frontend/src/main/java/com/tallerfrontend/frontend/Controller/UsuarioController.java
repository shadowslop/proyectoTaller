package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.SedeDto;
import com.tallerfrontend.frontend.model.UsuarioCreateRequest;
import com.tallerfrontend.frontend.model.UsuarioDto;
import com.tallerfrontend.frontend.service.SedeService;
import com.tallerfrontend.frontend.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsuarioController {

    @FXML private TextField campoNombreSede;
    @FXML private Label labelErrorSede;

    @FXML private TextField campoNombre;
    @FXML private PasswordField campoPassword;
    @FXML private ComboBox<String> comboRol;
    @FXML private ComboBox<SedeDto> comboSede;
    @FXML private Label labelError;
    @FXML private TableView<UsuarioDto> tablaUsuarios;
    @FXML private TableColumn<UsuarioDto, String> colNombre;
    @FXML private TableColumn<UsuarioDto, String> colRol;

    private final UsuarioService usuarioService = new UsuarioService();
    private final SedeService sedeService = new SedeService();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        comboRol.setItems(FXCollections.observableArrayList("ADMIN", "SUPERADMIN"));

        cargarSedes();
        cargarUsuarios();
    }

    @FXML
    private void handleCrearSede() {
        String nombre = campoNombreSede.getText();

        if (nombre.isBlank()) {
            mostrarErrorSede("Escribe un nombre para la sede");
            return;
        }

        SedeDto nueva = new SedeDto();
        nueva.setNombre(nombre);

        try {
            sedeService.crear(nueva);
            campoNombreSede.clear();
            labelErrorSede.setVisible(false);
            cargarSedes(); // refresca el combo de sedes para que aparezca la nueva
        } catch (Exception e) {
            mostrarErrorSede("No se pudo crear la sede");
        }
    }

    private void cargarSedes() {
        try {
            comboSede.setItems(FXCollections.observableArrayList(sedeService.listar()));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar las sedes");
        }
    }

    private void cargarUsuarios() {
        try {
            tablaUsuarios.setItems(FXCollections.observableArrayList(usuarioService.listar()));
        } catch (Exception e) {
            mostrarError("No se pudieron cargar los usuarios");
        }
    }

    @FXML
    private void handleCrear() {
        String nombre = campoNombre.getText();
        String password = campoPassword.getText();
        String rol = comboRol.getValue();
        SedeDto sede = comboSede.getValue();

        if (nombre.isBlank() || password.isBlank() || rol == null || sede == null) {
            mostrarError("Completa todos los campos");
            return;
        }

        UsuarioCreateRequest nuevo = new UsuarioCreateRequest(nombre, password, rol, sede.getId());

        try {
            usuarioService.crear(nuevo);
            limpiarFormulario();
            cargarUsuarios();
        } catch (Exception e) {
            mostrarError("No se pudo crear el usuario (revisa si el nombre ya existe)");
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoPassword.clear();
        comboRol.setValue(null);
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