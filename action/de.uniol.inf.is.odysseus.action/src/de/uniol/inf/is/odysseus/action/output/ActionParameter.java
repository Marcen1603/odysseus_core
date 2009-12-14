package de.uniol.inf.is.odysseus.action.output;

/**
 * Class representing a parameter of an IActuator method
 * @author Simon
 *
 */
public class ActionParameter {
	/**
	 * Enum describing the type of a parameter.
	 * Attribute for referencing the index of a tuple or
	 * Value for a standard java object
	 * @author Simon
	 *
	 */
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

	public Class<?> getParamClass() {
		if (value instanceof ActionAttribute){
			return ((ActionAttribute)value).getDatatype(); 
		}
		return this.value.getClass();
	}

}
