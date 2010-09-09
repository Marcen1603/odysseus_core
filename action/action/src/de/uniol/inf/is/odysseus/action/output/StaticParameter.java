package de.uniol.inf.is.odysseus.action.output;

/**
 * A static parameter represents a single atomic value
 * @author Simon Flandergan
 *
 */
public class StaticParameter implements IActionParameter {
	private Object value;
	private Class<?> paramClass;

	public StaticParameter(Object value) {
		this.value = value;
		this.paramClass = value.getClass();
	}

	@Override
	public Class<?> getParamClass() {
		return this.paramClass;
	}

	@Override
	public ParameterType getType() {
		return ParameterType.Value;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

}
