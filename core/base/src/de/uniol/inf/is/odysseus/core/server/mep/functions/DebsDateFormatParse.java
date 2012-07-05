package de.uniol.inf.is.odysseus.core.server.mep.functions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class DebsDateFormatParse extends AbstractFunction<Long> {

	private static final long serialVersionUID = 2729527379800655630L;

	@Override
	public int getArity() {
		return 1;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(" has only 1 argument.");
		}
		return accTypes;
	}

	@Override
	public String getSymbol() {
		return "debsDateFormatParse";
	}

	@Override
	public Long getValue() {
		String time = getInputValue(0);
		try {
			String onlytime = time.substring(0, time.length() - 6);
			String timeAndMillis = onlytime.substring(0, onlytime.length() - 4);
			String nano = time.substring(time.length() - 1);
			String micros = onlytime.substring(onlytime.length() - 4,
					onlytime.length() - 1);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			Date parsed = format.parse(timeAndMillis + " +0100");
			long millis = parsed.getTime();

			long nanoLong = Long.parseLong(nano);
			long microsLong = Long.parseLong(micros);
			long ts = (((TimeUnit.MILLISECONDS.toMicros(millis) + microsLong) * 10) + nanoLong) * 100;
			return ts;
		} catch (Exception e) {
			throw new RuntimeException("Input cannot be parsed "+time);
		}
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.LONG;
	}

}
