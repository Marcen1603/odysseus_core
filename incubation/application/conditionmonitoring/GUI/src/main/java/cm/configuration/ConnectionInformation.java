package cm.configuration;

import cm.communication.dto.AttributeInformation;

import java.util.List;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class ConnectionInformation {

    private String ip;
    private int queryId;
    private String queryName;
    private boolean useName;
    private List<AttributeInformation> attributes;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public boolean isUseName() {
        return useName;
    }

    public void setUseName(boolean useName) {
        this.useName = useName;
    }

    public List<AttributeInformation> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeInformation> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ConnectionInformation that = (ConnectionInformation) o;

        if (queryId != that.queryId)
            return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null)
            return false;
        return !(attributes != null ? !attributes.equals(that.attributes) : that.attributes != null);

    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + queryId;
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }
}
