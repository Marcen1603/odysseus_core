package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.timetag.ThreeOctetBinaryTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Is104TimeTagSubstituted extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = -7249429459387274973L;

	private static final Logger logger = LoggerFactory.getLogger(Is104TimeTagSubstituted.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Is104TimeTagSubstituted.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.BOOLEAN;

	/**
	 * Creates a new MEP function.
	 */
	public Is104TimeTagSubstituted() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Boolean getValue() {
		if (!(getInputValue(0) instanceof ITimeTag)) {
			logger.error("'{}' is not a timetag!", (Object) getInputValue(0));
			return null;
		}

		ITimeTag tt = (ITimeTag) getInputValue(0);
		if (tt instanceof ThreeOctetBinaryTime) {
			return ((ThreeOctetBinaryTime) tt).isSubstituted();
		} else {
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}