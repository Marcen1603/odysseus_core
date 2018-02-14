package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class HourStart extends AbstractUnaryDateFunction<Date> {

	private static final long serialVersionUID = -6745977420912101273L;

	public HourStart() {
		super("hourStart", SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Calendar in = Calendar.getInstance();
		in.setTime(getInputValue(0));
		Calendar calendar = Calendar.getInstance();
		calendar.set(in.get(Calendar.YEAR), in.get(Calendar.MONTH) + 1, in.get(Calendar.DAY_OF_MONTH), in.get(Calendar.HOUR), 0, 0);
		return calendar.getTime();
	}

}
