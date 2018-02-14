package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class NextHourStart extends AbstractUnaryDateFunction<Date> {

	private static final long serialVersionUID = -6745977420912101273L;

	public NextHourStart() {
		super("nextHourStart", SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Calendar in = Calendar.getInstance();
		in.setTime(getInputValue(0));
		in.add(Calendar.HOUR, 1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(in.get(Calendar.YEAR), in.get(Calendar.MONTH), in.get(Calendar.DAY_OF_MONTH), in.get(Calendar.HOUR), 0, 0);
		return calendar.getTime();
	}

}
