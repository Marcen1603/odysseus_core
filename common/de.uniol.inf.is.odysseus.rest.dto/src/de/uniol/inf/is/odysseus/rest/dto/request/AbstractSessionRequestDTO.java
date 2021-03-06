package de.uniol.inf.is.odysseus.rest.dto.request;

import de.uniol.inf.is.odysseus.rest.dto.response.AbstractResponseDTO;

/**
 * @author Tobias
 * @since 29.04.2015.
 */
public class AbstractSessionRequestDTO extends AbstractResponseDTO {

    private String token;

    public AbstractSessionRequestDTO() {

    }

    public AbstractSessionRequestDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}