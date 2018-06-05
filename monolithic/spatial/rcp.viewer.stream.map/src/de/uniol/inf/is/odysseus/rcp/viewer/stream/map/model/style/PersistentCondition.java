package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.style;

import java.io.Serializable;

public class PersistentCondition implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7332475177496338419L;
	
	public Object defaultValue = null;
	public String expression = null;

	public PersistentCondition(Object defaultValue, String expression) {
		this.defaultValue = defaultValue;
		this.expression = expression;
	}
	public PersistentCondition(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

}