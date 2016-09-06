package cm.model;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Tobias
 * @since 15.03.2015.
 */
public class Collection {

    private String name;
    private List<SocketInfo> connections;
    private UUID identifier;

    // For the background color
    private SocketInfo colorConnection;
    private String colorAttribute;
    private double minValue;
    private double maxValue;

    public Collection(String name, UUID identifier) {
        this.name = name;
        this.connections = new ArrayList<>();
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addConnection(SocketInfo connection) {
        this.connections.add(connection);
    }

    public List<SocketInfo> getConnections() {
        return connections;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public SocketInfo getColorConnection() {
        return colorConnection;
    }

    public void setColorConnection(SocketInfo colorConnection) {
        this.colorConnection = colorConnection;
    }

    public String getColorAttribute() {
        return colorAttribute;
    }

    public void setColorAttribute(String colorAttribute) {
        this.colorAttribute = colorAttribute;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
}
