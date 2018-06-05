package de.uniol.inf.is.odysseus.rest.dto.response;

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
