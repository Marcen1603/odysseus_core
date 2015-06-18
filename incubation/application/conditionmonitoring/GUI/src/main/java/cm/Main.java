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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"), bundle);
        Parent root = loader.load();
        MainController mainController = loader.getController();
        primaryStage.setTitle("Condition Monitoring");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();


        // Get configurations from Odysseus
        Configuration config = null;
        List<ConfigurationDescription> descriptions = RestService.getConfigurations("127.0.0.1", "System", "manager");
        if (descriptions != null) {
            descriptions.stream().forEach(listElement -> System.out.println(listElement.getId() + ", " + listElement.getName() + ", " + listElement.getDescription()));
            config = RestService.runConfiguration("127.0.0.1", "System", "manager", descriptions.get(0).getId());
        }

        if (config == null)
            return;

        // -----------------------------
        // Create GUI from configuration
        // -----------------------------


        // Build GUI from this configuration
        // ---------------------------------

        // Establish all connections
        for (ConnectionInformation conInformation : config.connectionInformation) {
            CommunicationService.establishConnection(conInformation);
        }

        // Create all collections
        for (CollectionInformation collectionInfo : config.collections) {
            Collection collection = new Collection(collectionInfo.name, Collection.OK_STATE);
            for (ConnectionInformation conInfo : collectionInfo.connectionInformation) {
                List<SocketReceiver> socketReceivers = CommunicationService.getSocketReceivers(conInfo);
                for (SocketReceiver receiver : socketReceivers) {
                    collection.addConnection(receiver.getSocketInfo());
                }
            }
            DataHandler.getInstance().addCollection(collection);
        }

        // Create all visualizations

        // 1. Gauges
        OverviewController overviewController = mainController.getOverviewController();

        for (VisualizationInformation visualizationInfo : config.visualizationInformation) {
            overviewController.addGauge(visualizationInfo);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
