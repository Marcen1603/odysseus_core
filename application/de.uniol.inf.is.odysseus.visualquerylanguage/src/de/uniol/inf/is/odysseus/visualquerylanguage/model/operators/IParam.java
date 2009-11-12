package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

public interface IParam<T> {
	
	public Collection<String> getTypeList();
	public String getType();
	public T getValue();
	public void setValue(T value);
	public void setName(String name);
	public String getName();
	public String getEditor();
	public void setEditor(String editor);
	
}
