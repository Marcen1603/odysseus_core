package de.uniol.inf.is.odysseus.mep.functions.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Marco Grawunder
 */

public class ToTimestampFromStringFunction extends AbstractFunction<Long> {

	private static final long serialVersionUID = 6255887477026357429L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.STRING, SDFDatatype.START_TIMESTAMP_STRING },
			new SDFDatatype[] { SDFDatatype.STRING } };

	public ToTimestampFromStringFunction() {
		super("toTimestamp", 2, accTypes, SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {
		String dateString = getInputValue(0).toString();
		String dateFormatString = getInputValue(1).toString();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(dateFormatString);
		return ToTimestampFromStringFunction.convertToMs(dateString, dateFormat, null).longValue();
	}

	public static Number convertToMs(String timeString, DateTimeFormatter df, ZoneId timezone) {
		Number timeN = null;
		LocalDateTime ldt = LocalDateTime.parse(timeString, df);
		ZonedDateTime zdt;
		if (timezone != null) {
			zdt = ldt.atZone(timezone);
		} else {
			zdt = ldt.atZone(ZoneId.of("UTC"));
		}

		timeN = zdt.toInstant().toEpochMilli();

		return timeN;
	}

}
