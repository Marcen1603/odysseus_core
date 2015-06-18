package cm.communication.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class CMConfigurationListResponseDTO {

    private List<ConfigurationDescription> configurations;

    public CMConfigurationListResponseDTO() {
        this.configurations = new ArrayList<ConfigurationDescription>();
    }

    public List<ConfigurationDescription> getConfigurations() {
        return configurations;
    }

    public void addConfiguration(ConfigurationDescription configuration) {
        this.configurations.add(configuration);
    }

}