package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class NextMonthStart extends AbstractUnaryDateFunction<Date> {

	private static final long serialVersionUID = -6745977420912101273L;

	public NextMonthStart() {
		super("nextMonthStart", SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Calendar in = Calendar.getInstance();
		in.setTime(getInputValue(0));
		Calendar calendar = Calendar.getInstance();
		int month = in.get(Calendar.MONTH) + 2;
		int year = in.get(Calendar.YEAR);
		if (month > 12) {
			month = 1;
			year = year + 1;
		}
		calendar.set(year, month, 1, 0, 0, 0);
		return calendar.getTime();
	}

}
