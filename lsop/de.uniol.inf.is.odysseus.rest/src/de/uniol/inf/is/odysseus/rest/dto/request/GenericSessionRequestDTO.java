package de.uniol.inf.is.odysseus.rest.dto.request;

public class GenericSessionRequestDTO<T> extends AbstractSessionRequestDTO {
	private T value;
	
	
	public GenericSessionRequestDTO(T value) {
		this.value = value;
	}
	public GenericSessionRequestDTO() {
		
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	

}
