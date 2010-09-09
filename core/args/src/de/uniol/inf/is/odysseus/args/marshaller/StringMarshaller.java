package de.uniol.inf.is.odysseus.args.marshaller;

import java.util.ListIterator;

import de.uniol.inf.is.odysseus.args.ArgsException;

/**
 * @author Jonas Jacobi
 */
public class StringMarshaller implements IParameterMarshaller {

	String value;
	
	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void parse(ListIterator<String> args) throws ArgsException {
		if (!args.hasNext()) {
			throw new ArgsException("missing parameter");
		}
		
		this.value = args.next();
	}

}
