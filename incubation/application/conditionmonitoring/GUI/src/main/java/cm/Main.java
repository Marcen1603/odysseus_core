package cm;

import cm.communication.CommunicationService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.*;
import cm.controller.MainController;
import cm.controller.OverviewController;
import cm.data.ConnectionHandler;
import cm.data.DataHandler;
import cm.model.Collection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.Socket;
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

        // -----------------------------
        // Create GUI from configuration
        // -----------------------------

        // Demo configuration
        // ------------------
        Configuration config = new Configuration();

        // Connections to the right queries
        List<ConnectionInformation> connectionInformation = new ArrayList<>();

        ConnectionInformation con1 = new ConnectionInformation();
        con1.ip = "127.0.0.1";
        con1.queryId = 0;

        connectionInformation.add(con1);
        config.connectionInformation = connectionInformation;

        // Collections
        List<CollectionInformation> collections = new ArrayList<>();

        CollectionInformation collectionInformation = new CollectionInformation();
        collectionInformation.name = "Machine1";
        List<ConnectionInformation> connectionInformationList = new ArrayList<>();
        connectionInformationList.add(con1);
        collectionInformation.connectionInformation = connectionInformationList;

        CollectionInformation collectionInformation2 = new CollectionInformation();
        collectionInformation2.name = "Machine2";
        List<ConnectionInformation> connectionInformationList2 = new ArrayList<>();
        connectionInformationList2.add(con1);
        collectionInformation2.connectionInformation = connectionInformationList2;

        collections.add(collectionInformation);
        collections.add(collectionInformation2);
        config.collections = collections;

        // Visualizations
        List<VisualizationInformation> visualizations = new ArrayList<>();

        VisualizationInformation visualizationInformation = new VisualizationInformation();
        visualizationInformation.connectionInformation = con1;
        visualizationInformation.attribute = "LOF";
        visualizationInformation.visualizationType = VisualizationType.GAUGE;
        visualizationInformation.minValue = 0.0;
        visualizationInformation.maxValue = 1.0;

        visualizations.add(visualizationInformation);
        config.visualizationInformation = visualizations;

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
