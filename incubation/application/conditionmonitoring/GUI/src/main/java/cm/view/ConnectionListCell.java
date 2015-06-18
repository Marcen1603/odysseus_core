package cm.view;

import cm.communication.dto.AttributeInformation;
import cm.communication.dto.SocketInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Tobias
 * @since 16.06.2015.
 */
public class ConnectionListCell extends ListCell<SocketInfo> {

    private Parent root;

    @FXML
    CheckBox newConnectionCheck;
    @FXML
    Label newConnectionAttributes;
    @FXML
    TextField newConnectionName;

    private SocketInfo socketInfo;

    public ConnectionListCell() {
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConnectionListCell.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(SocketInfo info, boolean empty) {
        super.updateItem(info, empty);
        if (empty || info == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        this.socketInfo = info;
        String attributes = "";
        for (AttributeInformation attributeInformation : info.getSchema()) {
            attributes += attributeInformation.getName() + ": " + attributeInformation.getDataType() + "\n";
        }
        newConnectionAttributes.setText(attributes);
        newConnectionName.setText(info.getName());
        setGraphic(root);
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public boolean isCellSelected() {
        return newConnectionCheck.isSelected();
    }

    public String getNameOfConnection() {
        if (newConnectionName != null)
            return newConnectionName.getText();
        return "";
    }
}