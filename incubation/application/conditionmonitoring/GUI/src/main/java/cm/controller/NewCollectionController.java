package cm.controller;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;
import cm.data.ConnectionHandler;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.view.ConnectionListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 16.06.2015.
 */
public class NewCollectionController {

    List<ConnectionListCell> connectionListCells = new ArrayList<>();

    @FXML
    TextField newCollectionName;

    @FXML
    ListView<SocketInfo> newCollectionConnectionList;

    @FXML
    private void initialize() {
        newCollectionConnectionList.setItems(ConnectionHandler.getInstance().getSocketInfos());
        newCollectionConnectionList.setCellFactory(listView -> {
            ConnectionListCell listCell = new ConnectionListCell();
            connectionListCells.add(listCell);
            return listCell;
        });
    }

    public void addCollection(ActionEvent actionEvent) {
        String name = newCollectionName.getText();
        Collection collection = new Collection(name);
        DataHandler.getInstance().addCollection(collection);

        for (ConnectionListCell listCell : connectionListCells) {
            if (listCell.isCellSelected()) {
                collection.addConnection(listCell.getSocketInfo());
            }
        }
    }
}
