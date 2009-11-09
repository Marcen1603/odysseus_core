/**
 * 
 */
package de.uniol.inf.is.odysseus.parser.pql;

/**
 * @author Jonas Jacobi
 */
public interface Parameter<T> {
	public static enum REQUIREMENT { MANDATORY, OPTIONAL };
	public Class<T> getType();
	public String getName();
	public Parameter.REQUIREMENT getRequirement();
	public void setValueOf(Object object);
	public void setNoValueAvailable();
	public T getValue();
	public boolean hasValue();
	public boolean isMandatory();
}