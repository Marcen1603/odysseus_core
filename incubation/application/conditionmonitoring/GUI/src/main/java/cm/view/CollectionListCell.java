package cm.view;

import cm.communication.dto.SocketInfo;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.Event;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * @author Tobias
 * @since 15.03.2015.
 */
public class CollectionListCell extends ListCell<Collection> implements ListChangeListener<Event> {

    @FXML
    Label collectionName;
    @FXML
    AnchorPane collectionBackground;

    boolean alreadyListening = false;
    private Parent root;
    private String colorAttribute = "";
    private double maxValue;
    private double minValue;
    private SocketInfo colorSocket;

    public CollectionListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("collectionListCell.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(Collection item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            Collection collection = getListView().getItems().get(super.getIndex());
            if (!alreadyListening && collection.getColorConnection() != null) {
                DataHandler.getInstance().getObservableEventList().addListener(this);
                colorAttribute = collection.getColorAttribute();
                minValue = collection.getMinValue();
                maxValue = collection.getMaxValue();
                colorSocket = collection.getColorConnection();
                alreadyListening = true;
            }

            collectionName.setText(collection.getName());
            collectionBackground.setStyle("-fx-background-color: #97b329;");
            setGraphic(root);
        }
    }

    @Override
    public void onChanged(Change<? extends Event> c) {
        while (c.next()) {
            if (c.wasAdded() && c.getAddedSize() > 0) {
                Event newEvent = c.getAddedSubList().get(0);
                if (!newEvent.getConnection().getSocketInfo().equals(colorSocket) || newEvent.getAttributes().get(colorAttribute) == null) {
                    // We listen to a whole collection, but only need
                    return;
                }
                String valueString = newEvent.getAttributes().get(colorAttribute);
                double value = Double.parseDouble(valueString);

                double segmentSize = (maxValue - minValue) / 10;
                int segment = 0;
                if (segmentSize > 0)
                    segment = (int) ((value - minValue) / segmentSize);

                String color = "-fx-background-color: #f61319;";
                switch (segment) {
                    case 0:
                        color = "-fx-background-color: #97b329;";
                        break;
                    case 1:
                        color = "-fx-background-color: #aacc2a;";
                        break;
                    case 2:
                        color = "-fx-background-color: #d4ea35;";
                        break;
                    case 3:
                        color = "-fx-background-color: #f2de31;";
                        break;
                    case 4:
                        color = "-fx-background-color: #fccb2e;";
                        break;
                    case 5:
                        color = "-fx-background-color: #f3a429;";
                        break;
                    case 6:
                        color = "-fx-background-color: #f18c23;";
                        break;
                    case 7:
                        color = "-fx-background-color: #f65821;";
                        break;
                    case 8:
                        color = "-fx-background-color: #f3351f;";
                        break;
                    case 9:
                        color = "-fx-background-color: #f61319;";
                        break;
                }
                collectionBackground.setStyle(color);
            }
        }
    }
}
