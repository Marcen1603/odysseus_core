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
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MainController{


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
        collectionEventList.setItems(DataHandler.getInstance().getObservableEventList());
        collectionEventList.setCellFactory(listView -> new EventListCell());
    }

    public void updateCollectionView(Collection collection) {
        // Just update the existing viewElements
        collectionName.setText(collection.getName());
        eventList.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        eventList.setCellFactory(listView -> new EventListCell());

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
            OverviewController overviewController = loader.getController();

            overviewPaneMap.put(collection, root);
            overviewControllerMap.put(collection, overviewController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public OverviewController getOverviewController() {
        return overviewIncludeController;
    }

    public OverviewController getOverviewIncludeControllerForCollection(Collection collection) {
        return overviewControllerMap.get(collection);
    }
}
