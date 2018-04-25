package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Split104TimeTag extends AbstractFunction<Tuple<IMetaAttribute>> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Split104TimeTag.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Split104TimeTag.class.getSimpleName();

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
	public Split104TimeTag() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Tuple<IMetaAttribute> getValue() {
		if (!(getInputValue(0) instanceof InformationElementSequence)) {
			logger.error("'{}' is not an information element sequence!", (Object) getInputValue(0));
			return null;
		}

		InformationElementSequence seq = (InformationElementSequence) getInputValue(0);
		Tuple<IMetaAttribute> tuple = new Tuple<>(2, false);
		tuple.setAttribute(0, seq.getInformationElements());
		tuple.setAttribute(1, seq.getTimeTag().orElse(null));
		return tuple;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}