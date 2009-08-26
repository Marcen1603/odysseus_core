package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParamSetter<T> implements IParamSetter<T>{
	
	private String type;
	private T value;
	private String setter;
	private String name = "";
	
	private static final Logger logger = LoggerFactory
	.getLogger(DefaultParamSetter.class);
	
	public DefaultParamSetter (String type, String setter, String name) {
		this.type = type;
		this.setter = setter;
		this.name = name;
	}

	@Override
	public String getType() {
		return type;
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

}
