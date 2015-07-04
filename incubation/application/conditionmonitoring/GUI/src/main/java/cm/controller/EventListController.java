package cm.controller;

import cm.data.DataHandler;
import cm.model.Event;
import cm.view.EventListCell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 23.06.2015.
 */
public class EventListController {

    @FXML
    public TextField eventsFilterField;
    @FXML
    ListView<Event> eventList;
    private ObservableList<Event> listOfEvents;

    @FXML
    private void initialize() {

        ObservableList<Event> events = this.listOfEvents != null ? this.listOfEvents : DataHandler.getInstance().getObservableEventList();
        eventList.setItems(events);

        eventList.setCellFactory(listView -> new EventListCell());
        eventsFilterField.textProperty().addListener(event -> {
            ObservableList<Event> correctEvents = this.listOfEvents != null ? this.listOfEvents : DataHandler.getInstance().getObservableEventList();
            if (eventsFilterField.getText().length() > 0) {
                ObservableList<Event> filteredEventList = correctEvents.filtered(event1 -> {

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
                    List<String> filterList = new ArrayList<>();
                    filterList.add(">=");
                    filterList.add("<=");
                    filterList.add(">");
                    filterList.add("<");
                    filterList.add("=");

                    for (String filter : filterList) {
                        if (filterText.contains(filter)) {
                            specialFilter = filter;
                            break;
                        }
                    }

                    try {
                        if (!specialFilter.isEmpty()) {
                            String trimFilterText = filterText.trim().replace(" ", "");
                            String key = trimFilterText.substring(0, trimFilterText.indexOf(specialFilter));
                            String valueString = trimFilterText.substring(trimFilterText.indexOf(specialFilter) + specialFilter.length(), trimFilterText
                                    .length());
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
                                        case ">=":
                                            return attributeValue >= value;
                                        case "<=":
                                            return attributeValue <= value;
                                    }

                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // We can't parse the string to a double. Just don't do anything.
                    }


                    return false;
                });
                eventList.setItems(filteredEventList);
            } else {
                ObservableList<Event> collEvents = this.listOfEvents != null ? this.listOfEvents : DataHandler.getInstance().getObservableEventList();
                eventList.setItems(collEvents);
            }
        });
    }

    public void setItems(ObservableList<Event> items) {
        this.listOfEvents = items;
        eventList.setItems(items);
    }
}
