package cm.view;

import cm.model.Sensor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;

/**
 * @author Tobias
 * @since 07.04.2015.
 */
public class SensorListCell extends ListCell<Sensor> {

    private Parent root;

    @FXML
    Label sensorName;

    @FXML Label sensorValue;

    public SensorListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sensor.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(Sensor item, boolean empty) {

        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            //Sensor sensor = getListView().getItems().get(super.getIndex());
            sensorName.setText(item.getName());
            sensorValue.setText(item.getCurrentValue().getValue() + "");

            sensorValue.textProperty().bindBidirectional(item.getCurrentValue(), new NumberStringConverter());

            setGraphic(root);
        }

    }
}
