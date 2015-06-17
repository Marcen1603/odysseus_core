package cm.communication.dto;

/**
 * @author Tobias
 * @since 29.04.2015.
 */
public class CreateSocketRequestDTO extends AbstractSessionRequestDTO {

    private int rootPort;
    private int queryId;
    private String queryName;
    private boolean useName;

    public CreateSocketRequestDTO() {
        this.useName = false;
    }

    public CreateSocketRequestDTO(String token, int queryId) {
        super(token);
        this.queryId = queryId;
        this.useName = false;
    }

    public CreateSocketRequestDTO(String token, String queryName) {
        super(token);
        this.queryName = queryName;
        this.useName = true;
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

}