package cm.controller;

import cm.communication.CommunicationService;
import cm.communication.dto.ConfigurationDescription;
import cm.communication.rest.RestException;
import cm.communication.rest.RestService;
import cm.view.ConfigurationDescriptionListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class ConfigChooseController {

    @FXML
    ListView<ConfigurationDescription> availableConfigs;

    @FXML
    private void initialize() {
        try {
            List<ConfigurationDescription> configs = RestService.getConfigurations(CommunicationService.getLoginIp(), CommunicationService.getToken());
            ObservableList<ConfigurationDescription> observableList = FXCollections.observableList(configs);
            availableConfigs.setItems(observableList);
            availableConfigs.setCellFactory(listView -> {
                ConfigurationDescriptionListCell listCell = new ConfigurationDescriptionListCell();
                return listCell;
            });
        } catch (RestException e) {
            e.printStackTrace();
        }
    }

    public void openConfiguration(ActionEvent actionEvent) {
        ConfigurationDescription config = availableConfigs.getSelectionModel().getSelectedItem();
        try {
            RestService.runConfiguration(CommunicationService.getLoginIp(), CommunicationService.getToken(), config.getId());
        } catch (RestException e) {
            e.printStackTrace();
        }

    }
}
