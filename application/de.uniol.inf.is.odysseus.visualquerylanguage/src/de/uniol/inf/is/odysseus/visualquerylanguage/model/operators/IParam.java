package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

public interface IParam<T> {
	
	public String getType();
	public T getValue();
	public void setValue(T value);
	public void setName(String name);
	public String getName();
	
}
