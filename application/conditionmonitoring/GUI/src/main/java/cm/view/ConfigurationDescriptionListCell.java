package cm.view;

import cm.communication.dto.ConfigurationDescription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class ConfigurationDescriptionListCell extends ListCell<ConfigurationDescription> {

    private Parent root;

    @FXML
    Label configName;
    @FXML
    Label configDescription;

    public ConfigurationDescriptionListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("configDescriptionListCell.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateItem(ConfigurationDescription config, boolean empty) {
        super.updateItem(config, empty);
        if (empty || config == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        this.configName.setText(config.getName());
        this.configDescription.setText(config.getDescription());
        setGraphic(root);
    }
}
