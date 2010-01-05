package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.operator.EventDetectionPO;

/**
 * Interface describing a parameter for the method of an {@link Action}
 * @see Action
 * @see EventDetectionPO
 * @author Simon Flandergan
 *
 */
public interface IActionParameter {
	public enum ParameterType{Attribute, Value};
		
	public Object getValue();

	public Class<?> getParamClass();

	public ParameterType getType();
}
