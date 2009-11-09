package de.uniol.inf.is.odysseus.args.marshaller;

import java.util.ListIterator;

import de.uniol.inf.is.odysseus.args.ArgsException;

/**
 * @author Jonas Jacobi
 */
public class CharacterMarshaller implements IParameterMarshaller {

	Character value;

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void parse(ListIterator<String> args) throws ArgsException {
		if (!args.hasNext()) {
			throw new ArgsException("missing parameter");
		}

		String value = args.next();
		if (value.length() != 1) {
			throw new ArgsException("illegal value for character parameter");
		}

		this.value = value.charAt(0);
	}

}
