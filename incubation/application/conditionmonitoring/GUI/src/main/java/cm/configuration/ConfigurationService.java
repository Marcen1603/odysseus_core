package cm.configuration;

import cm.communication.CommunicationService;
import cm.communication.dto.AttributeInformation;
import cm.communication.socket.SocketReceiver;
import cm.controller.MainController;
import cm.controller.OverviewController;
import cm.data.DataHandler;
import cm.model.Collection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class ConfigurationService {

    private static ConfigurationService instance;

    private MainController mainController;

    public final static String HIDDEN_OVERVIEW_NAME = "OverviewHidden";

    private ConfigurationService() {

    }

    public static ConfigurationService getInstance() {
        if (instance == null) {
            instance = new ConfigurationService();
        }
        return instance;
    }

    public void loadConfiguration(Configuration configuration, Stage stage) {
        try {
            InputStream inputStream = getClass().getResource("../bundles/Language_en.properties").openStream();
            ResourceBundle bundle = new PropertyResourceBundle(inputStream);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main.fxml"), bundle);

            Parent root = loader.load();
            MainController mainController = loader.getController();
            this.mainController = mainController;
            stage.setTitle("Condition Monitoring");
            stage.setScene(new Scene(root, 600, 500));

            // -----------------------------
            // Create GUI from configuration
            // -----------------------------

            // Establish all connections
            // -------------------------
            for (ConnectionInformation conInformation : configuration.getConnectionInformation()) {
                CommunicationService.establishConnection(CommunicationService.getToken(), conInformation);
            }

            // Create all collections
            // ----------------------
            for (CollectionInformation collectionInfo : configuration.getCollections()) {
                Collection collection = new Collection(collectionInfo.getName(), collectionInfo.getIdentifier());
                if (collectionInfo.getCollectionColoringInformation() != null) {
                    List<SocketReceiver> socketReceivers = CommunicationService.getSocketReceivers(collectionInfo.getCollectionColoringInformation()
                            .getConnectionInformation());
                    // If the connection information is used with enough information (operatorname and outputport), we get only one socketreceiver.
                    // Therefore, the correct connection will be used. If we get more than one receiver, we have to search and need a bit luck, that the
                    // given attribute is used only once in all output schemas. 
                    for (SocketReceiver receiver : socketReceivers) {
                        for (AttributeInformation attributeInformation : receiver.getSocketInfo().getSchema()) {
                            if (attributeInformation.getName().equals(collectionInfo.getCollectionColoringInformation().getAttribute())) {
                                collection.setColorConnection(receiver.getSocketInfo());
                                collection.setColorAttribute(collectionInfo.getCollectionColoringInformation().getAttribute());
                                collection.setMinValue(collectionInfo.getCollectionColoringInformation().getMinValue());
                                collection.setMaxValue(collectionInfo.getCollectionColoringInformation().getMaxValue());
                            }
                        }

                    }
                }

                // Connections
                for (ConnectionInformation conInfo : collectionInfo.getConnectionInformation()) {
                    List<SocketReceiver> socketReceivers = CommunicationService.getSocketReceivers(conInfo);
                    for (SocketReceiver receiver : socketReceivers) {
                        collection.addConnection(receiver.getSocketInfo());
                    }
                }

                // Visualizations
                mainController.addCollection(collection);

                if (collectionInfo.getGaugeVisualizationInformation() != null) {
                    OverviewController overviewController = mainController.getOverviewControllerForCollection(collection);
                    collectionInfo.getGaugeVisualizationInformation().forEach(overviewController::addGauge);
                }
                if (collectionInfo.getAreaChartVisualizationInformation() != null) {
                    OverviewController overviewController = mainController.getOverviewControllerForCollection(collection);
                    collectionInfo.getAreaChartVisualizationInformation().forEach(overviewController::addAreaChart);
                }

                DataHandler.getInstance().addCollection(collection);
            }

            // Overview
            // -------------------------
            if (configuration.getOverviewInformation() != null) {
                OverviewController overviewController = mainController.getOverviewController();
                configuration.getOverviewInformation().getGaugeVisualizationInformation().forEach(overviewController::addGauge);
                configuration.getOverviewInformation().getAreaChartVisualizationInformation().forEach(overviewController::addAreaChart);

                // Overview has an event list which we can handle as a collection
                if (configuration.getOverviewInformation().getOverviewConnections() != null) {
                    Collection overviewCollection = new Collection(HIDDEN_OVERVIEW_NAME, UUID.randomUUID());
                    for (ConnectionInformation conInfo : configuration.getOverviewInformation().getOverviewConnections()) {
                        List<SocketReceiver> socketReceivers = CommunicationService.getSocketReceivers(conInfo);
                        for (SocketReceiver receiver : socketReceivers) {
                            overviewCollection.addConnection(receiver.getSocketInfo());
                        }
                    }
                    DataHandler.getInstance().addCollection(overviewCollection);
                    mainController.addOverviewEventList(overviewCollection);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainController getMainController() {
        return mainController;
    }
}
