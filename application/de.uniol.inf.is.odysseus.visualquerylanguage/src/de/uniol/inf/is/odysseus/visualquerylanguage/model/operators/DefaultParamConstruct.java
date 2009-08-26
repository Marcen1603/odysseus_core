package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParamConstruct<T> implements IParamConstruct<T> {

	private int position;
	private String type;
	private T value;
	private String name = "";

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultParamConstruct.class);

	public DefaultParamConstruct(String type, int position, String name) {
		this.type = type;
		this.position = position;
		this.name = name;
	}

	@Override
	public int getPosition() {
		return position;
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

}
