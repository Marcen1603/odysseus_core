package cm.controller;

import cm.controller.listeners.CollectionListViewListener;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.Event;
import cm.view.EventListCell;
import cm.view.CollectionListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MainController{

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
    ListView<Event> collectionEventList;
    @FXML
    Label collectionName;
    @FXML
    Pane collectionOverviewPane;

    private Map<Collection, Parent> overviewPaneMap;
    private Map<Collection, OverviewController> overviewControllerMap;

    // Events tab
    @FXML
    ListView<Event> eventList;

    public MainController() {
        overviewPaneMap = new HashMap<>();
        overviewControllerMap = new HashMap<>();
    }

    @FXML
    private void initialize() {

        // Collections tab
        // ---------------
        collectionList.setItems(DataHandler.getInstance().getObservableCollectionList());
        collectionList.setCellFactory(listView -> new CollectionListCell());
        // To react to clicks on the listView of collections
        collectionList.getSelectionModel().getSelectedItems().addListener(new CollectionListViewListener(this));

        // Events tab
        // ----------
        eventList.setItems(DataHandler.getInstance().getObservableEventList());
        eventList.setCellFactory(listView -> new EventListCell());
    }

    public void updateCollectionView(Collection collection) {
        // For the first click from the overview if the UI was not loaded before
        if (collection == null)
            return;
        // Just update the existing viewElements
        collectionName.setText(collection.getName());
        collectionEventList.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        collectionEventList.setCellFactory(listView -> {
            // Select the first cell to not show an empty collection
            if (collectionList.getItems().size() > 0 && collectionList.getSelectionModel().getSelectedItems().isEmpty()) {
                collectionList.getSelectionModel().select(0);
            }
            return new EventListCell();
        });

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
            InputStream inputStream = getClass().getResource("../bundles/Language_en.properties").openStream();
            ResourceBundle bundle = new PropertyResourceBundle(inputStream);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/overview.fxml"), bundle);
            Parent root = loader.load();

            // Overview
            OverviewController overviewController = loader.getController();
            overviewPaneMap.put(collection, root);
            overviewControllerMap.put(collection, overviewController);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
