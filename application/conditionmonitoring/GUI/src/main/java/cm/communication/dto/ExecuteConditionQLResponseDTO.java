package cm.communication.dto;


/**
 * @author Tobias
 * @since 25.04.2015.
 */
public class ExecuteConditionQLResponseDTO extends AbstractResponseDTO {

    private String token;
    private SocketInfo socketInfo;
    private int queryId;
    private int restPort;


    public ExecuteConditionQLResponseDTO() {

    }

    public ExecuteConditionQLResponseDTO(String token, SocketInfo socketInfo, int queryId, int restPort) {
        this.token = token;
        this.socketInfo = socketInfo;
        this.queryId = queryId;
        this.restPort = restPort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public void setSocketInfo(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public int getRestPort() {
        return restPort;
    }

    public void setRestPort(int restPort) {
        this.restPort = restPort;
    }
}
