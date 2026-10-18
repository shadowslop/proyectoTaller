package com.tallerfrontend.frontend.Controller;

import com.tallerfrontend.frontend.model.LoginRequest;
import com.tallerfrontend.frontend.model.LoginResponse;
import com.tallerfrontend.frontend.service.ApiClient;
import com.tallerfrontend.frontend.service.ApiException;
import com.tallerfrontend.frontend.util.SesionActual;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoPassword;
    @FXML private Button botonLogin;
    @FXML private Label labelError;

    private final ApiClient apiClient = new ApiClient();

    @FXML
    private void handleLogin() {
        String nombre = campoUsuario.getText();
        String password = campoPassword.getText();

        if (nombre.isBlank() || password.isBlank()) {
            mostrarError("Completa usuario y contraseña");
            return;
        }

        botonLogin.setDisable(true);

        try {
            LoginResponse resp = apiClient.post("/api/auth/login", new LoginRequest(nombre, password), LoginResponse.class);

            SesionActual.getInstancia().iniciarSesion(
                    resp.getToken(), resp.getUsuarioId(), resp.getNombre(), resp.getRol(), resp.getSedeId()
            );

            irADashboard();

        } catch (ApiException e) {
            mostrarError("Usuario o contraseña incorrectos");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("No se pudo conectar con el servidor: " + e.getMessage());
        }
        finally {
            botonLogin.setDisable(false);
        }
    }

    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }

    private void irADashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tallerfrontend/frontend/dashboard-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) botonLogin.getScene().getWindow();
        stage.setScene(new Scene(root, 1100, 700));
        stage.setMinWidth(900);      // evita que la ventana se haga demasiado pequeña
        stage.setMinHeight(550);
        stage.setTitle("Panel de administracion");
    }


}