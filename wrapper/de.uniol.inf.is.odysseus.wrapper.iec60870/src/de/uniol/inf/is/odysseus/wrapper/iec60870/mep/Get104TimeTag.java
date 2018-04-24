package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Get104TimeTag extends AbstractFunction<ITimeTag> {

	private static final long serialVersionUID = -2969523156697135666L;

	private static final Logger logger = LoggerFactory.getLogger(Get104TimeTag.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Get104TimeTag.class.getSimpleName();

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
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Get104TimeTag() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public ITimeTag getValue() {
		if (!(getInputValue(0) instanceof InformationElementSequence)) {
			logger.error("'{}' is not an information element sequence!", (Object) getInputValue(0));
			return null;
		}

		InformationElementSequence seq = (InformationElementSequence) getInputValue(0);
		return seq.getTimeTag().orElse(null);
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}