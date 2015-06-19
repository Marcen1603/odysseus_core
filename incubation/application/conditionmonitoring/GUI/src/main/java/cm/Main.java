package cm;

import cm.communication.CommunicationService;
import cm.communication.dto.ConfigurationDescription;
import cm.communication.rest.RestService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.*;
import cm.controller.MainController;
import cm.controller.OverviewController;
import cm.data.ConnectionHandler;
import cm.data.DataHandler;
import cm.model.Collection;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        InputStream inputStream = getClass().getResource("bundles/Language_en.properties").openStream();
        ResourceBundle bundle = new PropertyResourceBundle(inputStream);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login.fxml"), bundle);
        Parent login = loader.load();
        primaryStage.setTitle("Login - Condition Monitoring");
        primaryStage.setScene(new Scene(login, 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
