package cm.communication.dto;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class RunConfigurationRequestDTO {

    private int configurationId;
    private String token;

    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
