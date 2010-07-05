package de.uniol.inf.is.odysseus.parser.pql;

/**
 * @author Jonas Jacobi
 */
public interface IParameterFactory<T> {
	public IParameter<T> createParameter(String name, IParameter.REQUIREMENT requirement);
}
