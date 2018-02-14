package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class NextDayStart extends AbstractUnaryDateFunction<Date> {

	private static final long serialVersionUID = -6745977420912101273L;

	public NextDayStart() {
		super("nextDayStart", SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Calendar in = Calendar.getInstance();
		in.setTime(getInputValue(0));
		in.add(Calendar.DAY_OF_MONTH, 1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(in.get(Calendar.YEAR), in.get(Calendar.MONTH), in.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return calendar.getTime();
	}

}
