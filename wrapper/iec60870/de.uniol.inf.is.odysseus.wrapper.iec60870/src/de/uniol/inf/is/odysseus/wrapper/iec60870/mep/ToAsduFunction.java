package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.List;

import de.uniol.inf.ei.oj104.model.ASDU;
import de.uniol.inf.ei.oj104.model.DataUnitIdentifier;
import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to put a {@link DataUnitIdentifier} and a list of
 * {@link IInformationObject}s together to an {@link ASDU}.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class ToAsduFunction extends AbstractFunction<ASDU> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 2803354313839623626L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "ToAsdu";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.LIST } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public ToAsduFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public ASDU getValue() {
		DataUnitIdentifier dataUnitIdentifier = getInputValue(0);
		List<IInformationObject> informationObjects = getInputValue(1);
		return new ASDU(dataUnitIdentifier, informationObjects);
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}