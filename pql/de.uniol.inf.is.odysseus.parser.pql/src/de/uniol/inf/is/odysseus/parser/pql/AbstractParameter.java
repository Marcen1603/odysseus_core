package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;


/**
 * @author Jonas Jacobi
 */
public abstract class AbstractParameter<T> implements IParameter<T>{

	final private String name;
	final private REQUIREMENT requirement;
	private T value;
	private IAttributeResolver resolver;

	public AbstractParameter(String name, REQUIREMENT requirement) {
		this.name = name;
		this.requirement = requirement;
		value = null;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT getRequirement() {
		return this.requirement;
	}
	
	@Override
	public boolean isMandatory() {
		return this.requirement == REQUIREMENT.MANDATORY;
	}

	@Override
	public void setNoValueAvailable() {
		this.value = null;
	}
	
	@Override
	public T getValue() {
		return value;
	}
	
	@Override
	public boolean hasValue() {
		return this.value != null;
	}
	
	protected void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public IAttributeResolver getAttributeResolver() {
		return this.resolver;
	}
	
	@Override
	public void setAttributeResolver(IAttributeResolver resolver) {
		this.resolver = resolver;
	}
}
