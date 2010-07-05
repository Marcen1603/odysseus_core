package de.uniol.inf.is.odysseus.parser.pql;

/**
 * @author Jonas Jacobi
 */
public class DirectParameter<T> extends AbstractParameter<T> {

	public DirectParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValueOf(Object object) {
		setValue((T) object);
	}
	
}
