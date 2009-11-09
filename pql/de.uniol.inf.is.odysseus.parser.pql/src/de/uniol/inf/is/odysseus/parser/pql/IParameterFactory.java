package de.uniol.inf.is.odysseus.parser.pql;

/**
 * @author Jonas Jacobi
 */
public interface IParameterFactory<T> {
	public Parameter<T> createParameter(String name, Parameter.REQUIREMENT requirement);
}
