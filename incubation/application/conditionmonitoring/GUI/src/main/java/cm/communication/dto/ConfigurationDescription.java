package cm.communication.dto;

import java.util.UUID;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class ConfigurationDescription {

    private UUID identifier;
    private String name;
    private String description;

    public ConfigurationDescription(UUID identifier, String name, String description) {
        super();
        this.identifier = identifier;
        this.name = name;
        this.description = description;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
