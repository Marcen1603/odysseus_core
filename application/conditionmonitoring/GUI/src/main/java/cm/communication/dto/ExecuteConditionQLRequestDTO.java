package cm.communication.dto;

/**
 * @author Tobias
 * @since 25.04.2015.
 */
public class ExecuteConditionQLRequestDTO extends AbstractRequestDTO {

    private String username;
    private String password;
    private String conditionQL;
    private String token;

    public ExecuteConditionQLRequestDTO(String username, String password, String conditionQL, String token) {
        this.username = username;
        this.password = password;
        this.conditionQL = conditionQL;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConditionQL() {
        return conditionQL;
    }

    public String getToken() {
        return token;
    }
}
