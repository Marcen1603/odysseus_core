package cm.communication.dto;

/**
 * @author Tobias
 * @since 29.04.2015.
 */
public class CreateSocketRequestDTO extends AbstractSessionRequestDTO {

    private int rootPort;

    private int queryId;


    public CreateSocketRequestDTO() {

    }

    public CreateSocketRequestDTO(String token, int queryId, int rootPort) {
        super(token);
        this.queryId = queryId;
        this.rootPort = rootPort;
    }

    public int getRootPort() {
        return rootPort;
    }

    public void setRootPort(int rootPort) {
        this.rootPort = rootPort;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

}
