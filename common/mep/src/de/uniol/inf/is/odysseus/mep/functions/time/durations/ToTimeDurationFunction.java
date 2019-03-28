package de.uniol.inf.is.odysseus.mep.functions.time.durations;

import java.time.Duration;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToTimeDurationFunction extends AbstractFunction<Duration> {

	private static final long serialVersionUID = 3425141448864587656L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.INTEGER, SDFDatatype.LONG },
			{ SDFDatatype.INTEGER, SDFDatatype.LONG }, { SDFDatatype.INTEGER, SDFDatatype.LONG },
			{ SDFDatatype.INTEGER, SDFDatatype.LONG }, { SDFDatatype.INTEGER, SDFDatatype.LONG },
			{ SDFDatatype.INTEGER, SDFDatatype.LONG } };

	public ToTimeDurationFunction() {
		super("toTimeDuration", 6, accTypes, SDFDatatype.DURATION);
	}

	@Override
	public Duration getValue() {
		long days = this.getInputValue(0);
		long hours = this.getInputValue(1);
		long minutes = this.getInputValue(2);
		long seconds = this.getInputValue(3);
		long milliseconds = this.getInputValue(4);
		long nanoseconds = this.getInputValue(5);
		return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusMillis(milliseconds)
				.plusNanos(nanoseconds);
	}

}
