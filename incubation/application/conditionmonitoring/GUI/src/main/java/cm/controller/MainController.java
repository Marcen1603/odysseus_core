package cm.controller;

import cm.configuration.ConfigurationService;
import cm.controller.listeners.CollectionListViewListener;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.view.CollectionListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    AnchorPane overviewEventInclude;
    @FXML
    EventListController overviewEventIncludeController;

    @FXML
    TabPane mainTabPane;

    // Overview tab
    @FXML
    Pane overviewInclude;
    @FXML
    OverviewController overviewIncludeController;

    // Collections tab
    @FXML
    ListView<Collection> collectionList;
    @FXML
    Label collectionName;
    @FXML
    Pane collectionOverviewPane;
    @FXML
    EventListController eventsCollectionIncludeController;

    private Map<Collection, Parent> overviewPaneMap;
    private Map<Collection, OverviewController> overviewControllerMap;

    public MainController() {
        overviewPaneMap = new HashMap<>();
        overviewControllerMap = new HashMap<>();
    }

    @FXML
    private void initialize() {

        // Collections tab
        // ---------------
        // We add the overview as a collection, but as it's shown on the main overview tab, we don't want to show it in the collections tab
        collectionList.setItems(DataHandler.getInstance().getObservableCollectionList().filtered(collection -> !collection.getName().equals
                (ConfigurationService.HIDDEN_OVERVIEW_NAME)));
        collectionList.setCellFactory(listView -> {
            // Select the first cell to not show an empty collection
            if (collectionList.getItems().size() > 0 && collectionList.getSelectionModel().getSelectedItems().isEmpty()) {
                collectionList.getSelectionModel().select(0);
            }
            return new CollectionListCell();
        });
        // To react to clicks on the listView of collections
        collectionList.getSelectionModel().getSelectedItems().addListener(new CollectionListViewListener(this));
    }

    public void updateCollectionView(Collection collection) {
        // For the first click from the overview if the UI was not loaded before
        if (collection == null)
            return;
        // Just update the existing viewElements
        collectionName.setText(collection.getName());
        eventsCollectionIncludeController.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        //collectionEventList.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        //collectionEventList.setCellFactory(listView -> new EventListCell());

        // Load the correct viewElement (overview)
        if (overviewPaneMap.get(collection) != null) {
            // We have a pane for this collection
            if (collectionOverviewPane.getChildren().size() > 0) {
                collectionOverviewPane.getChildren().remove(0);
            }
            collectionOverviewPane.getChildren().add(overviewPaneMap.get(collection));
        }
    }

    public void addCollection(Collection collection) {
        try {
            if (!collection.getName().equals(ConfigurationService.HIDDEN_OVERVIEW_NAME)) {
                InputStream inputStream = getClass().getResource("../bundles/Language_en.properties").openStream();
                ResourceBundle bundle = new PropertyResourceBundle(inputStream);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/overview.fxml"), bundle);
                Parent root = loader.load();

                // Overview
                OverviewController overviewController = loader.getController();
                overviewPaneMap.put(collection, root);
                overviewControllerMap.put(collection, overviewController);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addOverviewEventList(Collection collection) {
        overviewEventIncludeController.setItems(DataHandler.getInstance().getCollectionEvents(collection));
    }

    public void switchToCollection(Collection collection) {
        // Switch to collection tab
        mainTabPane.getSelectionModel().select(1);

        // Select the right collection
        collectionList.getSelectionModel().select(collection);
    }


    public OverviewController getOverviewController() {
        return overviewIncludeController;
    }

    public OverviewController getOverviewControllerForCollection(Collection collection) {
        return overviewControllerMap.get(collection);
    }
}
