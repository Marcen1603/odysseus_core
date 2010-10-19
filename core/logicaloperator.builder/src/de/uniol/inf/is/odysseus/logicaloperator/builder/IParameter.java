/**
 * 
 */
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;

/**
 * @author Jonas Jacobi
 */
public interface IParameter<T> {
	public static enum REQUIREMENT { MANDATORY, OPTIONAL };
	public String getName();
	public IParameter.REQUIREMENT getRequirement();
	public void setInputValue(Object object);
	public boolean validate();
	public List<Exception> getErrors();
	public T getValue();
	public boolean hasValue();
	public boolean isMandatory();
	public void setAttributeResolver(IAttributeResolver resolver);
	public IAttributeResolver getAttributeResolver();
	public void clear();
}