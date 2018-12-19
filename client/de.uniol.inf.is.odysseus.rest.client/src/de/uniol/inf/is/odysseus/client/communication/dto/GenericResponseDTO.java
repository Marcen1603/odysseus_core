package de.uniol.inf.is.odysseus.client.communication.dto;

/**
 * @author Tobias
 * @since 25.04.2015.
 */
public class GenericResponseDTO<T> extends AbstractResponseDTO {

    private T value;


    public GenericResponseDTO(T value) {
        this.value = value;
    }

    public GenericResponseDTO() {

    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


}
