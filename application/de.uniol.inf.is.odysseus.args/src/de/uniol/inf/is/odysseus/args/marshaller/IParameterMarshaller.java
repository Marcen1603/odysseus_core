package de.uniol.inf.is.odysseus.args.marshaller;

import java.util.ListIterator;

import de.uniol.inf.is.odysseus.args.ArgsException;

/**
 * @author Jonas Jacobi
 */
public interface IParameterMarshaller {
	public void parse(ListIterator<String> args) throws ArgsException;

	public Object getValue();
}
