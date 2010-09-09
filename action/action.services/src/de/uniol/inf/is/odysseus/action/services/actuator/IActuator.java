package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.List;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * Interface describing the functions of an Actuator.
 * Actuators implement the logic of actions.
 * WARNING: Primitive types and Wrapper-Classes are _NOT_ distinguished by the responsible parser.
 * If you provide two methods with same parameter order but one with wrapper and one with primitive type
 * it is _UNDETERMINED_ which one will be executed, the first which has an equivalent schema! 
 * @author Simon Flandergan
 *
 */
public interface IActuator {
	/**
	 * Execute a method of this actuator. The associated method is determined
	 * by methodName and number and type of each parameter.
	 * @param method methodName
	 * @param types array containing each parameter type
	 * @param params values to deliver as parameter
	 * @throws ActuatorException
	 */
	public void executeMethod (String method, Class<?>[] types, Object[] params) throws ActuatorException;
	
	/**
	 * Returns ALL available methods of this actuator including parameters
	 * @see ActionMethod
	 * @return
	 */
	public List<ActionMethod> getFullSchema();
	
	/**
	 * Returns methods of this actuator marked as to show
	 * @see ActionMethod
	 * @return
	 */
	public List<ActionMethod> getReducedSchema();


}
