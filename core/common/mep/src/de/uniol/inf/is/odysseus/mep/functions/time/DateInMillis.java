package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class DateInMillis extends AbstractUnaryDateFunction<Long> {

	private static final long serialVersionUID = -6052633428152691443L;

	public DateInMillis() {
		super("dateInMillis", SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		return calendar.getTimeInMillis();
	}

}
