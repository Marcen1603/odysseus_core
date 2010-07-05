/**
 * 
 */
package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

/**
 * @author Jonas Jacobi
 */
public interface IParameter<T> {
	public static enum REQUIREMENT { MANDATORY, OPTIONAL };
	public String getName();
	public IParameter.REQUIREMENT getRequirement();
	public void setValueOf(Object object);
	public void setNoValueAvailable();
	public T getValue();
	public boolean hasValue();
	public boolean isMandatory();
	public void setAttributeResolver(IAttributeResolver resolver);
	public IAttributeResolver getAttributeResolver();
}