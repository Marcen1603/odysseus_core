package cm.controller;

import cm.controller.listeners.CollectionListViewListener;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.Event;
import cm.view.EventListCell;
import cm.view.CollectionListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MainController {

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
    @FXML
    public TextField eventsFilterField;

    public MainController() {
        overviewPaneMap = new HashMap<>();
        overviewControllerMap = new HashMap<>();
    }

    @FXML
    private void initialize() {

        // Collections tab
        // ---------------
        collectionList.setItems(DataHandler.getInstance().getObservableCollectionList());
        collectionList.setCellFactory(listView -> {
            // Select the first cell to not show an empty collection
            if (collectionList.getItems().size() > 0 && collectionList.getSelectionModel().getSelectedItems().isEmpty()) {
                collectionList.getSelectionModel().select(0);
            }
            return new CollectionListCell();
        });
        // To react to clicks on the listView of collections
        collectionList.getSelectionModel().getSelectedItems().addListener(new CollectionListViewListener(this));

        // Events tab
        // ----------
        eventList.setItems(DataHandler.getInstance().getObservableEventList());
        eventList.setCellFactory(listView -> new EventListCell());
        eventsFilterField.textProperty().addListener(event -> {
            if (eventsFilterField.getText().length() > 0) {
                ObservableList<Event> filteredEventList = DataHandler.getInstance().getObservableEventList().filtered(event1 -> {

                    String filterText = eventsFilterField.getText();

                    // Search for events which match the filter
                    if (event1.getConnection().getSocketInfo().getQueryName().toLowerCase().contains(filterText.toLowerCase()))
                        return true;

                    for (String key : event1.getAttributes().keySet()) {
                        if (key.toLowerCase().contains(filterText.toLowerCase()))
                            return true;
                    }

                    for (String attribute : event1.getAttributes().values()) {
                        if (attribute.toLowerCase().contains(filterText.toLowerCase())) {
                            return true;
                        }
                    }

                    String specialFilter = "";
                    List<String> filterList = new ArrayList<String>();
                    filterList.add(">");
                    filterList.add("<");
                    filterList.add("=");

                    for (String filter : filterList) {
                        if (filterText.contains(filter)) {
                            specialFilter = filter;
                            break;
                        }
                    }

                    if (!specialFilter.isEmpty()) {
                        String trimFilterText = filterText.trim().replace(" ", "");
                        String key = trimFilterText.substring(0, trimFilterText.indexOf(specialFilter));
                        String valueString = trimFilterText.substring(trimFilterText.indexOf(specialFilter) + specialFilter.length(), trimFilterText.length());
                        if (!valueString.isEmpty()) {
                            double value = Double.parseDouble(valueString);
                            String attributeValueString = event1.getAttributes().get(key);
                            if (attributeValueString != null && !attributeValueString.isEmpty()) {
                                double attributeValue = Double.parseDouble(attributeValueString);
                                switch (specialFilter) {
                                    case ">":
                                        return attributeValue > value;
                                    case "<":
                                        return attributeValue < value;
                                    case "=":
                                        return attributeValue == value;
                                }

                            }
                        }
                    }

                    return false;
                });
                eventList.setItems(filteredEventList);
            } else {
                eventList.setItems(DataHandler.getInstance().getObservableEventList());
            }
        });
    }

    public void updateCollectionView(Collection collection) {
        // For the first click from the overview if the UI was not loaded before
        if (collection == null)
            return;
        // Just update the existing viewElements
        collectionName.setText(collection.getName());
        collectionEventList.setItems(DataHandler.getInstance().getCollectionEvents(collection));
        collectionEventList.setCellFactory(listView -> new EventListCell());

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
