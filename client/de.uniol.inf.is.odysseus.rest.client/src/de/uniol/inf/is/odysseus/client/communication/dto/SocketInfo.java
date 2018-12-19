package de.uniol.inf.is.odysseus.client.communication.dto;

import java.util.List;

public class SocketInfo {
    private String ip;
    private int port;
    private List<AttributeInformation> schema;
    private String connectionName;
    private String description;

    public SocketInfo() {

    }

    public SocketInfo(String ip, int port, List<AttributeInformation> schema) {
        this.ip = ip;
        this.port = port;
        this.schema = schema;
    }

    public List<AttributeInformation> getSchema() {
        return schema;
    }

    public void setSchema(List<AttributeInformation> schema) {
        this.schema = schema;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocketInfo that = (SocketInfo) o;

        if (port != that.port) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        return !(schema != null ? !schema.equals(that.schema) : that.schema != null);

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        return result;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}