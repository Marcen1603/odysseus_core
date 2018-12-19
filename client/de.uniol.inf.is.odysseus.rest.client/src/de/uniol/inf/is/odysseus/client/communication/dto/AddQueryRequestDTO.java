package de.uniol.inf.is.odysseus.client.communication.dto;

/**
 * @author Tobias
 * @since 29.04.2015.
 */
public class AddQueryRequestDTO extends AbstractSessionRequestDTO {

    private String query;

    private String parser;

    private String transformationConfig;

    public AddQueryRequestDTO() {

    }

    public AddQueryRequestDTO(String token, String query, String parser, String transformationConfig) {
        super(token);
        this.query = query;
        this.parser = parser;
        this.transformationConfig = transformationConfig;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public String getTransformationConfig() {
        return transformationConfig;
    }

    public void setTransformationConfig(String transformationConfig) {
        this.transformationConfig = transformationConfig;
    }
}
