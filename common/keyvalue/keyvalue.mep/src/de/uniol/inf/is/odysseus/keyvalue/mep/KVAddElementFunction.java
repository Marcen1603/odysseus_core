package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KVAddElementFunction extends AbstractFunction<KeyValueObject<? extends IMetaAttribute>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4331195978641168427L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "add";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 3;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFKeyValueDatatype.KEYVALUEOBJECT },
			{ SDFDatatype.STRING }, SDFDatatype.SIMPLE_TYPES };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFKeyValueDatatype.KEYVALUEOBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public KVAddElementFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getValue() {
		KeyValueObject<?> obj = (KeyValueObject<?>) getInputValue(0);
		obj.setAttribute(getInputValue(1), getInputValue(2));
		return obj;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
