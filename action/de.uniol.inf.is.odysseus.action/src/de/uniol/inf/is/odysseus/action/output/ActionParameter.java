package de.uniol.inf.is.odysseus.action.output;

public class ActionParameter {
	public enum ParameterType{Attribute, Value}
	
	private ParameterType type;
	private Object value;
	
	public ActionParameter (ParameterType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public ParameterType getType() {
		return type;
	}
	
	public Object getValue() {
		return value;
	}

}
