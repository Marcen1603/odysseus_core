package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a Method of an {@link IActuator}. 
 * Used to provide schema for the parser and user.
 * @author Simon Flandergan
 *
 */
public class ActionMethod {
	private String name;
	private ArrayList<Class<?>> parameterTypes;
	private boolean showInSchema;
		
	/**
	 * Public constructor
	 * @param name name of the method
	 * @param classes class types of each parameter in order
	 * @param showInSchema flag for applications to reduce number of shown methods
	 */
	public ActionMethod (String name, Class<?>[] classes, boolean showInSchema){
		this.name = name;
		this.parameterTypes = new ArrayList<Class<?>>(Arrays.asList(classes));
		this.showInSchema = showInSchema;
	}
	
	/**
	 * return method name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * return classes for each parameter in order
	 * @return
	 */
	public Class<?>[] getParameterTypes() {
		Class<?>[] parameters = new Class<?>[this.parameterTypes.size()];
		return this.parameterTypes.toArray(parameters);
	}
	
	/**
	 * checks if this method is compatible to another method with 
	 * given name and parameters
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	public boolean isCompatibleTo(String methodName, Class<?>[] parameters){
		if (!this.name.equals(methodName) || parameters.length != this.parameterTypes.size()){
			//method name differs
			return false;
		}
		for (int i=0; i < parameters.length; i++){
			if (!PrimitivTypeComparator.sameType(parameters[i], this.parameterTypes.get(i))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * returns if schema should be shown
	 * @return
	 */
	public boolean isShowInSchema() {
		return showInSchema;
	}

}
