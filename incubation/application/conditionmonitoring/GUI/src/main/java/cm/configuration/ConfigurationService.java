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

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class ConfigurationService {

    private static ConfigurationService instance;

    private MainController mainController;

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
                    List<SocketReceiver> socketReceivers = CommunicationService.getSocketReceivers(collectionInfo.getCollectionColoringInformation().getConnectionInformation());
                    // As we can't distinguish between multiple outputs of one query, we have to do this awkward search (which hopefully, but not necessarily finds what we need)
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

            // Create all visualizations
            // -------------------------
            OverviewController overviewController = mainController.getOverviewController();
            configuration.getGaugeVisualizationInformation().forEach(overviewController::addGauge);
            configuration.getAreaChartVisualizationInformation().forEach(overviewController::addAreaChart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainController getMainController() {
        return mainController;
    }
}
