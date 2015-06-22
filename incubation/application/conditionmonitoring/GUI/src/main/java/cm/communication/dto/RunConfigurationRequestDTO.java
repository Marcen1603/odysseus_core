package cm.communication.dto;

import java.util.UUID;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class RunConfigurationRequestDTO {

    private UUID configurationId;
    private String token;

    public UUID getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(UUID configurationId) {
        this.configurationId = configurationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
