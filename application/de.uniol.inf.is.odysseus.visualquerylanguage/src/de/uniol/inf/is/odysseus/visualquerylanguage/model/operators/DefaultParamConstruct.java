package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParamConstruct<T> implements IParamConstruct<T> {
	
	private String type;
	private int position;
	private Collection<String> typeList;
	private T value;
	private String name = "";
	private boolean hasEditor = false;

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultParamConstruct.class);

	public DefaultParamConstruct(String type, Collection<String> typeList, int position, String name) {
		this.typeList = typeList;
		this.position = position;
		this.type = type;
		this.name = name;
	}

	@Override
	public int getPosition() {
		return position;
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
		if (value != null) {
			this.value = value;
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("value is null");
			}
		}
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
	public boolean hasEditor() {
		return this.hasEditor;
	}
	
	@Override
	public void setEditor(boolean hasEditor) {
		this.hasEditor = hasEditor;
	}

	@Override
	public String getType() {
		return this.type;
	}

}
