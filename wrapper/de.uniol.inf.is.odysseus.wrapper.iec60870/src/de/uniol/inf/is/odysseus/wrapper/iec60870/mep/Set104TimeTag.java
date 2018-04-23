package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.Calendar;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.ei.oj104.model.timetag.SevenOctetBinaryTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Set104TimeTag extends AbstractFunction<InformationElementSequence> {

	private static final long serialVersionUID = 7973125631862734798L;

	private static final Logger logger = LoggerFactory.getLogger(Set104TimeTag.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Set104TimeTag.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 4;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.LONG }, { SDFDatatype.BOOLEAN }, { SDFDatatype.BOOLEAN } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Set104TimeTag() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public InformationElementSequence getValue() {
		if (!(getInputValue(0) instanceof InformationElementSequence)) {
			logger.error("'{}' is not an information element sequence!", (Object) getInputValue(0));
			return null;
		}
		InformationElementSequence seq = (InformationElementSequence) getInputValue(0);
		long timestamp = getInputValue(1);
		boolean substituted = getInputValue(2);
		boolean invalid = getInputValue(3);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		ITimeTag timeTag = new SevenOctetBinaryTime(calendar.get(Calendar.MILLISECOND), calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MINUTE), substituted, invalid, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.YEAR));
		seq.setTimeTag(Optional.of(timeTag));
		return seq;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}