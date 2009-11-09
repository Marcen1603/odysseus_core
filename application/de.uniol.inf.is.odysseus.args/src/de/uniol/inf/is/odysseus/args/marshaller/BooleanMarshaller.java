package de.uniol.inf.is.odysseus.args.marshaller;

import java.util.ListIterator;

/**
 * @author Jonas Jacobi
 */
public class BooleanMarshaller implements IParameterMarshaller {

	private Boolean isSet = false;
	@Override
	public Boolean getValue() {
		return this.isSet;
	}

	@Override
	public void parse(ListIterator<String> args) {
		this.isSet = true;
	}

}
