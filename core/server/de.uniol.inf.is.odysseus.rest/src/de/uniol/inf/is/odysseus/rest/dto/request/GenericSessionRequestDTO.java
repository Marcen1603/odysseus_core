package de.uniol.inf.is.odysseus.rest.dto.request;

public class GenericSessionRequestDTO<T> extends SessionRequestDTO {
	private T value;
	
	
	public GenericSessionRequestDTO(String token, T value) {
		super(token);
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
