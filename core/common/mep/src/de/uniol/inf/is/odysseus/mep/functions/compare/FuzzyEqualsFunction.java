package de.uniol.inf.is.odysseus.mep.functions.compare;

import com.google.common.math.DoubleMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to check whether two doubles are equal with a given tolerance.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class FuzzyEqualsFunction extends AbstractFunction<Boolean> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -108000498383893929L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "FuzzyEquals";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 3;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.BOOLEAN;

	/**
	 * Creates a new MEP function.
	 */
	public FuzzyEqualsFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Boolean getValue() {
		double value1 = getInputValue(0);
		double value2 = getInputValue(1);
		double tolerance = getInputValue(2);

		return DoubleMath.fuzzyEquals(value1, value2, tolerance);
	}

}