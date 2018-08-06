package de.uniol.inf.is.odysseus.keyvalue.mep;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToKeyValueFromMapFunction extends AbstractFunction<KeyValueObject<? extends IMetaAttribute>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 3136720296992574123L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(ToKeyValueFromMapFunction.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "toKeyValue";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFKeyValueDatatype.KEYVALUEOBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public ToKeyValueFromMapFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@SuppressWarnings("unchecked") // caught with try catch
	@Override
	public KeyValueObject<? extends IMetaAttribute> getValue() {
		try {
			return KeyValueObject.createInstance((Map<String, Object>) getInputValue(0)); 
		} catch(Exception e) {
			log.error("Can not cast '{0}' to KeyValueObject! The parameter must be Map<String, Object>!", getInputValue(0), e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
