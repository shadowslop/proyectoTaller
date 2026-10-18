package com.tallerfrontend.frontend;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tallerfrontend/frontend/login-view.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("Iniciar sesion");
        stage.setMinWidth(360);
        stage.setMinHeight(380);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}