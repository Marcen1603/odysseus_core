package cm.view;

import cm.communication.dto.AttributeInformation;
import cm.communication.dto.SocketInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

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
        if (info == null)
            return;
        this.socketInfo = info;
        super.updateItem(info, empty);

        String attributes = "";
        for (AttributeInformation attributeInformation : info.getSchema()) {
            attributes += attributeInformation.getName() + ": " + attributeInformation.getDataType() + "\n";
        }
        newConnectionAttributes.setText(attributes);
        setGraphic(root);
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public boolean isCellSelected() {
        return newConnectionCheck.isSelected();
    }
}
