package cm.controller;

import cm.communication.dto.SocketInfo;
import cm.communication.rest.RestException;
import cm.communication.rest.RestService;
import cm.communication.socket.SocketReceiver;
import cm.data.ConnectionHandler;
import cm.view.ConnectionListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 16.06.2015.
 */
public class NewConnectionController {

    ObservableList<SocketInfo> observableSocketList = FXCollections.observableArrayList();
    List<ConnectionListCell> connectionListCells = new ArrayList<>();

    @FXML
    TextField queryIdField;

    @FXML
    TextField queryIPField;

    @FXML
    ListView<SocketInfo> socketInfoList;

    @FXML
    Button connectionWindowAddButton;

    public void sendRequest(ActionEvent actionEvent) {
        String queryIdString = queryIdField.getText();
        String queryIP = queryIPField.getText();
        int queryId = Integer.parseInt(queryIdString);

        try {
            String token = RestService.login(queryIP, "System", "manager");
            List<SocketInfo> socketInfos = RestService.getResultsFromQuery(queryIP, token, queryId);
            observableSocketList.clear();
            observableSocketList.addAll(socketInfos);
            socketInfoList.setItems(observableSocketList);
            socketInfoList.setCellFactory(listView -> {
                ConnectionListCell listCell = new ConnectionListCell();
                connectionListCells.add(listCell);
                return listCell;
            });
        } catch (RestException e) {
            e.printStackTrace();
        }
    }

    public void addConnections(ActionEvent actionEvent) {
        for (ConnectionListCell listCell : connectionListCells) {
            if (listCell.isCellSelected()) {
                SocketInfo socketInfo = listCell.getSocketInfo();
                socketInfo.setName(listCell.getNameOfConnection());
                SocketReceiver receiver = new SocketReceiver(socketInfo);
                ConnectionHandler.getInstance().addConnection(receiver);
            }
        }
        Stage stage = (Stage) connectionWindowAddButton.getScene().getWindow();
        stage.close();
    }
}
