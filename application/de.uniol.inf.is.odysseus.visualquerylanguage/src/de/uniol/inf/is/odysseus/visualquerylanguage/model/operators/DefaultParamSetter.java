package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParamSetter<T> implements IParamSetter<T>{
	
	private String type;
	private Collection<String> typeList;
	private T value;
	private String setter;
	private String name = "";
	private String editor = "";
	
	private static final Logger logger = LoggerFactory
	.getLogger(DefaultParamSetter.class);
	
	public DefaultParamSetter (String type, Collection<String> typeList, String setter, String name) {
		this.type = type;
		this.typeList = typeList;
		this.setter = setter;
		this.name = name;
	}

	@Override
	public Collection<String> getTypeList() {
		return typeList;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		if(value != null) {
			this.value = value;
		}else {
			if(logger.isWarnEnabled()) {
				logger.warn("value is null");
			}
		}
	}

	@Override
	public String getSetter() {
		return setter;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getEditor() {
		return this.editor;
	}
	
	@Override
	public void setEditor(String editor) {
		this.editor = editor;
	}

	@Override
	public String getType() {
		return this.type;
	}

}
