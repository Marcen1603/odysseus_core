package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KVToStringFunction extends AbstractFunction<String> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -779870258434299597L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "toString";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFKeyValueDatatype.KEYVALUEOBJECT } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFDatatype.STRING;

	/**
	 * Creates a new MEP function.
	 */
	public KVToStringFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public String getValue() {
		return ((KeyValueObject<?>) getInputValue(0)).toString();
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}