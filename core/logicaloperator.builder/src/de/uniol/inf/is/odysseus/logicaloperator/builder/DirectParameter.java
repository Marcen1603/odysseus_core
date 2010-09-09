package de.uniol.inf.is.odysseus.logicaloperator.builder;

/**
 * @author Jonas Jacobi
 */
public class DirectParameter<T> extends AbstractParameter<T> {

	public DirectParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		setValue((T) this.inputValue);
	}

}
