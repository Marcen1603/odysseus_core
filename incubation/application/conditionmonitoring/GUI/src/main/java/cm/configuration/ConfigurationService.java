package cm.configuration;

import javafx.fxml.FXMLLoader;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class ConfigurationService {

    public static void loadConfiguration(Configuration configuration) {
        //        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"), bundle);
//        Parent root = loader.load();
//        MainController mainController = loader.getController();
//        primaryStage.setTitle("Condition Monitoring");
//        primaryStage.setScene(new Scene(root, 600, 500));
//        primaryStage.show();


        // Get configurations from Odysseus
//        Configuration config = null;
//        List<ConfigurationDescription> descriptions = RestService.getConfigurations("127.0.0.1", "System", "manager");
//        if (descriptions != null) {
//            descriptions.stream().forEach(listElement -> System.out.println(listElement.getId() + ", " + listElement.getName() + ", " + listElement.getDescription()));
//            config = RestService.runConfiguration("127.0.0.1", "System", "manager", descriptions.get(0).getId());
//        }
//
//        if (config == null)
//            return;

        // -----------------------------
        // Create GUI from configuration
        // -----------------------------


        // Build GUI from this configuration
        // ---------------------------------

        // Establish all connections
//        for (ConnectionInformation conInformation : config.connectionInformation) {
//            CommunicationService.establishConnection(conInformation);
//        }
//
//        // Create all collections
//        for (CollectionInformation collectionInfo : config.collections) {
//            Collection collection = new Collection(collectionInfo.name, Collection.OK_STATE);
//            for (ConnectionInformation conInfo : collectionInfo.connectionInformation) {
//                List<SocketReceiver> socketReceivers = CommunicationService.getSocketReceivers(conInfo);
//                for (SocketReceiver receiver : socketReceivers) {
//                    collection.addConnection(receiver.getSocketInfo());
//                }
//            }
//            DataHandler.getInstance().addCollection(collection);
//        }
//
//        // Create all visualizations
//
//        // 1. Gauges
//        OverviewController overviewController = mainController.getOverviewController();
//
//        for (VisualizationInformation visualizationInfo : config.visualizationInformation) {
//            overviewController.addGauge(visualizationInfo);
//        }
    }
}
