package de.uniol.inf.is.odysseus.action.services.actuator;

/**
 * Helper class creating an association between attributeName and
 * classType
 * @author Simon Flandergan
 *
 */
public class ActionParameter {
	private String name;
	private Class<?> type;
	
	public ActionParameter(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}	
	
	public String getName() {
		return name;
	}
	
	public Class<?> getType() {
		return type;
	}
}
