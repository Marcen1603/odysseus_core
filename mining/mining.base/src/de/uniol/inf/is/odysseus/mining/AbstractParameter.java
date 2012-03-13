package de.uniol.inf.is.odysseus.mining;

public class AbstractParameter<T> implements IParameter<T> {

	private String name;
	private T value;
	
	public AbstractParameter(String name, T value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public T getValue() {
		return this.value;
	}

	@Override
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
