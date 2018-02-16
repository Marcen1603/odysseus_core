package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class QuarterStart extends AbstractUnaryDateFunction<Date> {

	private static final long serialVersionUID = -6745977420912101273L;

	public QuarterStart() {
		super("quarterStart", SDFDatatype.DATE);
	}

	@Override
	public Date getValue() {
		Calendar in = Calendar.getInstance();
		in.setTime(getInputValue(0));
		Calendar calendar = Calendar.getInstance();
		int quarterStartMonth = ((in.get(Calendar.MONTH)/3)*3);
		calendar.set(in.get(Calendar.YEAR), quarterStartMonth, 1, 0, 0, 0);
		return calendar.getTime();
	}

}
