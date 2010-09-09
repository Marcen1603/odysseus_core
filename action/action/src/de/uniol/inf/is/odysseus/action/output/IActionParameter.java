package de.uniol.inf.is.odysseus.action.output;

import de.uniol.inf.is.odysseus.action.operator.EventTriggerPO;

/**
 * Interface describing a parameter for the method of an {@link Action}
 * @see Action
 * @see EventTriggerPO
 * @author Simon Flandergan
 *
 */
public interface IActionParameter {
	public enum ParameterType{Attribute, Value};
		
	public Class<?> getParamClass();

	public ParameterType getType();

	public Object getValue();
}
