package cm.communication.dto;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class GetCMConfigurationListRequestDTO {

    private String token;

    public GetCMConfigurationListRequestDTO(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}