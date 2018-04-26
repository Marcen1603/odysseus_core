package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.timetag.SevenOctetBinaryTime;
import de.uniol.inf.ei.oj104.model.timetag.ThreeOctetBinaryTime;
import de.uniol.inf.ei.oj104.model.timetag.TwoOctetBinaryTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Get104TimeTagFromTimestamp extends AbstractFunction<ITimeTag> {

	private static final long serialVersionUID = -5760978473124259017L;

	private static final Logger logger = LoggerFactory.getLogger(Get104TimeTagFromTimestamp.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Get104TimeTagFromTimestamp.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 5;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.LONG }, { SDFDatatype.BOOLEAN }, { SDFDatatype.BOOLEAN }, { SDFDatatype.STRING } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Get104TimeTagFromTimestamp() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public ITimeTag getValue() {
		if (!(getInputValue(0) instanceof Class)) {
			logger.error("'{}' is not a time tag class!", (Object) getInputValue(0));
			return null;
		}

		Class<?> ttClass = (Class<?>) getInputValue(0);
		long ts = getInputValue(1);
		boolean substituted = getInputValue(2);
		boolean invalid = getInputValue(3);
		String tzID = (String) getInputValue(4);
		TimeZone tz = (tzID.equalsIgnoreCase("default")) ? TimeZone.getDefault() : TimeZone.getTimeZone(tzID);

		int milliseconds, seconds, minutes;
		milliseconds = (int) (ts % 1000);
		seconds = (int) (ts / 1000);
		minutes = seconds / 60;

		if (ttClass.equals(TwoOctetBinaryTime.class)) {
			return new TwoOctetBinaryTime(milliseconds, seconds);
		} else if (ttClass.equals(ThreeOctetBinaryTime.class)) {
			seconds %= 60;
			return new ThreeOctetBinaryTime(milliseconds, seconds, minutes, substituted, invalid);
		} else if (ttClass.equals(SevenOctetBinaryTime.class)) {
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTimeInMillis(ts);
			SevenOctetBinaryTime tt = new SevenOctetBinaryTime();
			tt.fromCalendar(calendar);
			return tt;
		} else {
			logger.error("'{}' is not a known time tag class!", (Object) getInputValue(0));
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}