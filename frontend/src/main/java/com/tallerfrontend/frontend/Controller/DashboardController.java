package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.service.DocumentoService;
import com.tallerfrontend.frontend.util.SesionActual;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label labelBienvenida;
    @FXML private StackPane panelContenido;
    @FXML private Button botonUsuarios;
    @FXML private Button botonReportes;
    @FXML private Button botonDocumentos;
    @FXML private Button botonTiposVehiculo;
    @FXML private Button botonMantenimientos;
    @FXML private Button botonAuditoria;

    // Banner de alertas de documentos (RF-20 / HU-04)
    @FXML private HBox bannerAlertas;
    @FXML private Label labelAlertas;

    private final DocumentoService documentoService = new DocumentoService();
    private final com.tallerfrontend.frontend.service.PrestamoService prestamoService =
            new com.tallerfrontend.frontend.service.PrestamoService();

    @FXML
    public void initialize() {
        String nombre = SesionActual.getInstancia().getNombre();
        String rol = SesionActual.getInstancia().getRol();
        labelBienvenida.setText("Hola, " + nombre + " (" + rol + ")");

        boolean esSuperAdmin = "SUPERADMIN".equals(rol);

        // Pantallas solo para el administrador (RF-22). Vehiculos ya NO se oculta:
        // todos pueden consultar disponibilidad; crear/eliminar lo controla VehiculoController.
        ocultarSiNo(botonUsuarios, esSuperAdmin);
        ocultarSiNo(botonReportes, esSuperAdmin);
        ocultarSiNo(botonDocumentos, esSuperAdmin);
        ocultarSiNo(botonTiposVehiculo, esSuperAdmin);
        ocultarSiNo(botonMantenimientos, esSuperAdmin); // RF-21: lo asigna el administrador
        ocultarSiNo(botonAuditoria, esSuperAdmin);

        cargarAlertas(esSuperAdmin);
    }

    private void ocultarSiNo(Button boton, boolean visible) {
        boton.setVisible(visible);
        boton.setManaged(visible);
    }

    // RF-20: avisa al administrador al entrar, sin que tenga que abrir Documentos.
    // Se consulta en un hilo aparte para no congelar la ventana si el backend tarda.
    private void cargarAlertasDocumentos() {
        Thread hilo = new Thread(() -> {
            try {
                int vencidos = documentoService.listarVencidos().size();
                int porVencer = documentoService.listarPorVencer().size();
                if (vencidos + porVencer > 0) {
                    Platform.runLater(() -> {
                        labelAlertas.setText("Alerta de documentos: " + vencidos + " vencido(s) y "
                                + porVencer + " por vencer en 30 dias");
                        bannerAlertas.setVisible(true);
                        bannerAlertas.setManaged(true);
                    });
                }
            } catch (Exception ignorado) {
                // Si falla la consulta no se muestra banner; el resto del dashboard sigue funcionando
            }
        });
        hilo.setDaemon(true);
        hilo.start();
    }

    @FXML
    private void mostrarClientes() {
        cargarVista("/com/tallerfrontend/frontend/clientes-view.fxml");
    }

    @FXML
    private void mostrarVehiculos() {
        cargarVista("/com/tallerfrontend/frontend/vehiculo-view.fxml");
    }

    @FXML
    private void mostrarTiposVehiculo() {
        cargarVista("/com/tallerfrontend/frontend/tipovehiculo-view.fxml");
    }

    @FXML
    private void mostrarDocumentos() {
        cargarVista("/com/tallerfrontend/frontend/documentos-view.fxml");
    }

    @FXML
    private void mostrarPrestamos() {
        cargarVista("/com/tallerfrontend/frontend/registro-view.fxml");
    }

    // Nueva seccion separada: las reservas ya no viven dentro de Prestamos
    @FXML
    private void mostrarReservas() {
        cargarVista("/com/tallerfrontend/frontend/reservas-view.fxml");
    }

    @FXML
    private void mostrarUsuarios() {
        cargarVista("/com/tallerfrontend/frontend/usuarios-view.fxml");
    }

    @FXML
    private void mostrarReportes() {
        cargarVista("/com/tallerfrontend/frontend/reportes-view.fxml");
    }

    @FXML
    private void mostrarMantenimientos() {
        cargarVista("/com/tallerfrontend/frontend/mantenimiento-view.fxml");
    }

    @FXML
    private void mostrarAuditoria() {
        cargarVista("/com/tallerfrontend/frontend/auditoria-view.fxml");
    }

    private void cargarVista(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();
            panelContenido.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace(); // muestra la causa real en consola
            panelContenido.getChildren().setAll(new Label("Error al cargar la vista"));
        }
    }

    @FXML
    private void cerrarSesion() throws IOException {
        SesionActual.getInstancia().cerrarSesion();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tallerfrontend/frontend/login-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) panelContenido.getScene().getWindow();
        stage.setScene(new Scene(root, 400, 400));
        stage.setMinWidth(360);
        stage.setMinHeight(380);
        stage.setTitle("Iniciar sesion");
    }
    private void cargarAlertas(boolean esSuperAdmin) {
        Thread hilo = new Thread(() -> {
            StringBuilder msg = new StringBuilder();

            if (esSuperAdmin) {
                try {
                    int vencidos = documentoService.listarVencidos().size();
                    int porVencer = documentoService.listarPorVencer().size();
                    if (vencidos + porVencer > 0) {
                        msg.append("Documentos: ").append(vencidos).append(" vencido(s) y ")
                                .append(porVencer).append(" por vencer en 30 dias");
                    }
                } catch (Exception ignorado) { }
            }

            try {
                int n = prestamoService.listarReservasVencidas().size();
                if (n > 0) {
                    if (msg.length() > 0) msg.append("   |   ");
                    msg.append(n).append(" prestamo(s) de reserva con mas de 7 dias de retraso");
                }
            } catch (Exception ignorado) { }

            if (msg.length() > 0) {
                Platform.runLater(() -> {
                    labelAlertas.setText(msg.toString());
                    bannerAlertas.setVisible(true);
                    bannerAlertas.setManaged(true);
                });
            }
        });
        hilo.setDaemon(true);
        hilo.start();
    }
}