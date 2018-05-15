package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class MinuteStart extends AbstractUnaryDateFunction<Date> {

	private static final long serialVersionUID = -6745977420912101273L;

	public MinuteStart() {
		super("minuteStart", SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Calendar in = Calendar.getInstance();
		in.setTime(getInputValue(0));
		Calendar calendar = Calendar.getInstance();
		calendar.set(in.get(Calendar.YEAR), in.get(Calendar.MONTH), in.get(Calendar.DAY_OF_MONTH), in.get(Calendar.HOUR), in.get(Calendar.MINUTE), 0);
		return calendar.getTime();
	}

}
