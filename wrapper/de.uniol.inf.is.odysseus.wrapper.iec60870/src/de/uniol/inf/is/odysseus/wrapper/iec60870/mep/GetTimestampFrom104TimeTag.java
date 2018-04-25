package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class GetTimestampFrom104TimeTag extends AbstractFunction<Long> {

	private static final long serialVersionUID = -2969523156697135666L;

	private static final Logger logger = LoggerFactory.getLogger(GetTimestampFrom104TimeTag.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = GetTimestampFrom104TimeTag.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 3;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT }, { SDFDatatype.STRING }, { SDFDatatype.INTEGER } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.LONG;

	/**
	 * Creates a new MEP function.
	 */
	public GetTimestampFrom104TimeTag() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Long getValue() {
		if (!(getInputValue(0) instanceof ITimeTag)) {
			logger.error("'{}' is not a timetag!", (Object) getInputValue(0));
			return null;
		}
		
		ITimeTag tt = (ITimeTag) getInputValue(0);
		String tzID = (String) getInputValue(1);
		TimeZone tz = (tzID.equalsIgnoreCase("default")) ? TimeZone.getDefault() : TimeZone.getTimeZone(tzID);
		int century = (int) getInputValue(2);
		
		if(tt == null) {
			return null;
		} else {
			return tt.getTimestamp(tz, century);
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}