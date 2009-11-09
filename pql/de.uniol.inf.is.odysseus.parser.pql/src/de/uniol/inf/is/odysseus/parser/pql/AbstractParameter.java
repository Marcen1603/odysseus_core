package de.uniol.inf.is.odysseus.parser.pql;


/**
 * @author Jonas Jacobi
 */
public abstract class AbstractParameter<T> implements Parameter<T>{

	final private String name;
	final private Class<T> type;
	final private REQUIREMENT requirement;
	private T value;

	public AbstractParameter(String name, Class<T> type, REQUIREMENT requirement) {
		this.name = name;
		this.type = type;
		this.requirement = requirement;
		value = null;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public de.uniol.inf.is.odysseus.parser.pql.Parameter.REQUIREMENT getRequirement() {
		return this.requirement;
	}
	
	@Override
	public boolean isMandatory() {
		return this.requirement == REQUIREMENT.MANDATORY;
	}

	@Override
	public Class<T> getType() {
		return this.type;
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
}
